package com.brotru.code.generator.templates.pebble;

import io.pebbletemplates.pebble.error.LoaderException;
import io.pebbletemplates.pebble.loader.Loader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 * @author bronek
 */
public class MemoryMapTemplateLoader implements Loader<String> {

    private final Map<String, String> templateMap;

    public MemoryMapTemplateLoader(Map<String, String> templateMap) {
        this.templateMap = templateMap;
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        String normalizedName = normalizePath(templateName);
        String content = templateMap.get(normalizedName);

        if (content == null) {
            throw new LoaderException(null, "Template not found in memory map: " + normalizedName);
        }
        return new StringReader(content);
    }

    @Override
    public boolean resourceExists(String templateName) {
        if (templateName == null) {
            return false;
        }
        return templateMap.containsKey(normalizePath(templateName));
    }

    @Override
    public void setCharset(String charset) {
        // Since we serve raw Strings directly from a Java Map in memory,
        // encoding translation is unnecessary. You can leave this empty safely.
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        if (relativePath == null) return null;

        // If it looks like an absolute path, normalize and return it directly
        if (relativePath.startsWith("/")) {
            return normalizePath(relativePath);
        }

        // If there's no base template anchoring it, it's a root path
        if (anchorPath == null || anchorPath.isEmpty()) {
            return normalizePath(relativePath);
        }

        // Handle relative traversal (e.g. "../macro/util.pebble" relative to "subfolder/template.pebble")
        try {
            Path parent = Paths.get(anchorPath).getParent();
            if (parent == null) {
                return normalizePath(relativePath);
            }
            Path resolved = parent.resolve(relativePath).normalize();
            return normalizePath(resolved.toString());
        } catch (Exception e) {
            // Fallback for non-standard file architectures
            return normalizePath(relativePath);
        }
    }

    @Override
    public String createCacheKey(String templateName) {
        return normalizePath(templateName);
    }

    // Helper to ensure map keys match uniformly regardless of operating system slashes
    private String normalizePath(String path) {
        if (path == null) return "";
        String normalized = path.replace("\\", "/");
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        return normalized;
    }

    @Override
    public void setPrefix(String prefix) {
    }

    @Override
    public void setSuffix(String suffix) {
    }
}