package com.brotru.code.generator;

import com.brotru.code.annotations.Expose;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.StringLiteralExpr;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mojo(name = "generate-code", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = org.apache.maven.plugins.annotations.ResolutionScope.COMPILE)
@org.apache.maven.plugins.annotations.Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class ExposeGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
    private File sourceDirectory;

    @Parameter(property = "singleFile")
    private String singleFile;

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
            List<String> generatedLines = new ArrayList<>();

            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (FieldDeclaration field : clazz.getFields()) {
                    processExposeAnnotations(field, generatedLines);
                }
            }

            if (generatedLines.isEmpty()) return;

            writeGeneratedBlock(generatedLines, content, path,
                    getBeginBlock(),
                    getEndBlock());

        } catch (Exception e) {
            getLog().error("Failed to process code block replacement for file: " + path, e);
        }
    }

    private String getBeginBlock() {
        return "// <editor-fold defaultstate=\"collapsed\" desc=\"Generated Code DO NOT EDIT\">\n// region Generated Code\n";
    }

    private String getEndBlock() {
        return "// endregion\n// </editor-fold>";
    }

    private void processExposeAnnotations(FieldDeclaration field, List<String> generatedLines) {
        if (field.isAnnotationPresent(Expose.class)) {
            final Optional<AnnotationExpr> annotation = field.getAnnotationByClass(Expose.class);
//            final String additionalJavadoc = getStringValue(annotation).orElse("").trim();
            final String additionalJavadoc = annotation
                    .flatMap(ann -> ann.findFirst(StringLiteralExpr.class))
                    .map(StringLiteralExpr::getValue)
                    .orElse("");
            String fieldName = field.getVariable(0).getNameAsString();
            String fieldType = field.getElementType().asString();
            String template = "    /**\n";
            if (!additionalJavadoc.isBlank()) {
                template = template + "     * " + additionalJavadoc + "\n";
            }
            template = template +
                    "     * Returns instance of {@link JsonTool}.\n" +
                    "     * See: {@link #json}\n" +
                    "     * @return \n" +
                    "     */\n" +
                    "    public %s %s() { return %s; }";

            // Construct the exact signature you requested
            String methodBlock = String.format(template,

                    fieldType, fieldName, fieldName);
            generatedLines.add(methodBlock);
        }
    }

    public static Optional<String> getStringValue(Optional<AnnotationExpr> annotationOpt) {
        if (!annotationOpt.isPresent()) {
            return Optional.empty();
        }

        AnnotationExpr annotation = annotationOpt.get();

        // 1. Handle Single Member Format: @Expose("myValue")
        if (annotation.isSingleMemberAnnotationExpr()) {
            return Optional.of(annotation.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().getValue());
        }

        // 2. Handle Normal Format: @Expose(value = "myValue")
        if (annotation.isNormalAnnotationExpr()) {
            for (MemberValuePair pair : annotation.asNormalAnnotationExpr().getPairs()) {
                if ("value".equals(pair.getNameAsString())) {
                    return Optional.of(pair.getValue().asStringLiteralExpr().getValue());
                }
            }
        }

        // 3. Handle Marker Format: @Expose (fallback to your default value if applicable)
        return Optional.empty();
    }

    private boolean writeGeneratedBlock(List<String> generatedMethods, String content, Path path, String beginBlockString,
            String endBlockString)
            throws IOException {
        // Build the block content
        StringBuilder blockBuilder = new StringBuilder();
        blockBuilder.append("\n\n").append(beginBlockString).append("\n");
        for (String method : generatedMethods) {
            blockBuilder.append(method);
        }
        blockBuilder.append(endBlockString).append("");
        String targetReplacementBlock = blockBuilder.toString();
        String updatedContent;
        String regex = "(\\s*" + beginBlockString + ")[\\s\\S]*?(" + endBlockString + ")";
        if (content.contains(beginBlockString) && content.contains(endBlockString)) {
            // Scenario A: Overwrite the existing code block entirely
            updatedContent = content.replaceAll(regex, targetReplacementBlock);
        } else {
            // Scenario B: Smart Insert if anchors do not exist yet
            // Find the last closing brace '}' of the class definition and insert before it
            int lastBraceIdx = content.lastIndexOf("}");
            if (lastBraceIdx == -1) {
                return true;
            }
            updatedContent = content.substring(0, lastBraceIdx)
                    + "\n" + targetReplacementBlock + "\n"
                    + content.substring(lastBraceIdx);
        }
        // Only update disk if content has structurally changed
        if (!content.equals(updatedContent)) {
            Files.writeString(path, updatedContent);
            getLog().info("Regenerated code injection block inside: " + path.getFileName());
        }
        return false;
    }
}
