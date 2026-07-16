package com.brotru.code.generator.templates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 *
 * @author bronek
 */
public class DirectoryTemplatesMapSupplier implements TemplatesMapSupplier {

    private final Path templatesRootDirectory;

    public DirectoryTemplatesMapSupplier(Path templatesRootDirectory) {
        this.templatesRootDirectory = templatesRootDirectory;
    }

    @Override
    public Map<String, String> get() {
        final Map<String, String> templateMap = new HashMap<>();
        try (Stream<Path> stream = Files.walk(templatesRootDirectory)) {
            stream
                    .filter(Files::isRegularFile) // Ignore directories themselves
                    .forEach(filePath -> {
                        try {
                            // 3. Calculate the relative path from the root template directory
                            // Example: "src/main/templates/fields/myTemplate.txt" -> "fields/myTemplate.txt"
                            Path relativePath = templatesRootDirectory.relativize(filePath);

                            // Replace OS-specific backslashes with standard forward slashes if needed
                            String key = relativePath.toString().replace('\\', '/');

                            // 4. Read the file contents entirely as a String
                            String content = Files.readString(filePath);

                            templateMap.put(key, content);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return templateMap;
    }

}
