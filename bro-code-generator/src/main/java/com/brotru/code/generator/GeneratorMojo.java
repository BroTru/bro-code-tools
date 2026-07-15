package com.brotru.code.generator;

import com.brotru.code.generator.logging.LogMojoImpl;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mojo(name = "generate-code", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = org.apache.maven.plugins.annotations.ResolutionScope.COMPILE)
@org.apache.maven.plugins.annotations.Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
    private File sourceDirectory;

    @Parameter(property = "singleFile")
    private String singleFile;

    @Parameter(defaultValue = "${project.basedir}", readonly = true, required = true)
    private File baseDir;

    // Optional: Allow users to override the template directory name in their POM configuration
    @Parameter(defaultValue = "src/main/templates")
    private String templateDirectoryPath;
    
    private final GeneratedBlockProcessor generatedBlockProcessor = new GeneratedBlockProcessor(new LogMojoImpl(getLog()).setPrefix("block-processor: "));

    private final ExposeProcessor expose = new ExposeProcessor();
    
    @Override
    public void execute() throws MojoExecutionException {
        if (singleFile != null && !singleFile.isEmpty()) {
            File targetFile = new File(singleFile);
            if (targetFile.exists() && targetFile.isFile()) {
                getLog().info("NetBeans Action Triggered - Processing Single File: " + targetFile.getName());
                processFile(targetFile.toPath());
            }
        } else {
            try (Stream<Path> paths = Files.walk(sourceDirectory.toPath())) {
                paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(this::processFile);
            } catch (Exception e) {
                throw new MojoExecutionException("Failed to run code generation block replacement", e);
            }
        }
    }

    private void processFile(Path path) {
        try {
            String content = Files.readString(path);
            if (!content.contains("@Expose")) return;

            CompilationUnit cu = StaticJavaParser.parse(content);
            final List<String> fragments = new ArrayList<>();

            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (FieldDeclaration field : clazz.getFields()) {
                    processField(field, fragments);
                }
            }

            if (fragments.isEmpty()) return;

            generatedBlockProcessor.writeGeneratedBlock(fragments, content, path,
                    getBeginBlock(),
                    getEndBlock());

        } catch (Exception e) {
            getLog().error("Failed to process code block replacement for file: " + path, e);
        }
    }

    /**
     * Single field processing.
     * @param field
     * @param fragments 
     */
    private void processField(FieldDeclaration field, final List<String> fragments) throws MojoExecutionException {
        expose.process(field, fragments);
        processFieldTemplates(field, fragments);
    }

    private void processFieldTemplates(FieldDeclaration field, final List<String> fragments) throws MojoExecutionException {
        File templateFolder = new File(baseDir, templateDirectoryPath);

        if (!templateFolder.exists() || !templateFolder.isDirectory()) {
            getLog().warn("Template directory not found at: " + templateFolder.getAbsolutePath());
            return;
        }

        getLog().info("Reading templates from: " + templateFolder.getAbsolutePath());
         Map<String, String> templateMap = new HashMap<>();

         Path templateRoot = new File(baseDir, templateDirectoryPath).toPath();
         
        // 2. Recursively walk through the directory tree
        try (Stream<Path> stream = Files.walk(templateRoot)) {
            stream
                .filter(Files::isRegularFile) // Ignore directories themselves
                .forEach(filePath -> {
                    try {
                        // 3. Calculate the relative path from the root template directory
                        // Example: "src/main/templates/fields/myTemplate.txt" -> "fields/myTemplate.txt"
                        Path relativePath = templateRoot.relativize(filePath);
                        
                        // Replace OS-specific backslashes with standard forward slashes if needed
                        String key = relativePath.toString().replace('\\', '/');
                        
                        // 4. Read the file contents entirely as a String
                        String content = Files.readString(filePath);
                        
                        templateMap.put(key, content);
                        getLog().debug("Loaded template: " + key);
                        
                    } catch (IOException e) {
                        getLog().error("Failed to read template file: " + filePath, e);
                    }
                });
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to scan template directory", e);
        }

        // 5. Use your map for generation logic
        getLog().info("Successfully loaded " + templateMap.size() + " templates.");

    }
    
    private String getBeginBlock() {
        return Const.GENERATED_BLOCK_BEGIN_TAG;
    }

    private String getEndBlock() {
        return Const.GENERATED_BLOCK_END_TAG;
    }



}
