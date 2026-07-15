package com.brotru.code.generator.templates.pebble;

import com.brotru.code.generator.logging.Log;
import com.brotru.code.generator.templates.TemplateProcessor;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author bronek
 */
public class TemplateProcessorPebbleImpl implements TemplateProcessor {

    private final Log log;
    private PebbleEngine engine;

    public TemplateProcessorPebbleImpl(Log log) {
        this.log = log;
    }

    @Override
    public TemplateProcessorPebbleImpl initialize(final Path templateRoot) throws IOException {
        return initialize(new DirectoryTemplatesMapProvider(templateRoot).get());
    }

    public TemplateProcessorPebbleImpl initialize(final Map.Entry<String, String>... templates) {
        final Map<String, String> templatesMap = new HashMap<>(templates.length);
        for (Map.Entry<String, String> template : templates) {
            templatesMap.put(template.getKey(), template.getValue());
        }
        initialize(templatesMap);
        return this;
    }

    public TemplateProcessorPebbleImpl initialize(final Map<String, String> templatesMap) {
        engine = new PebbleEngine.Builder()
                .loader(new MemoryMapTemplateLoader(templatesMap))
                .cacheActive(false)
                .build();

        return this;
    }

    @Override
    public String evaluate(FieldDeclaration field, String templateName) throws IOException {
        Objects.requireNonNull(engine, "Enginge is null, was initialize() method called ?");
        return evaluate(templateName, createFieldContext(field));
    }

    public String evaluate(String templateName, Map<String, Object> context) throws IOException {
        PebbleTemplate template = engine.getTemplate(templateName);

        StringWriter writer = new StringWriter();
        template.evaluate(writer, context);

        return writer.toString();
    }

    private Map<String, Object> createFieldContext(FieldDeclaration field) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }
}
