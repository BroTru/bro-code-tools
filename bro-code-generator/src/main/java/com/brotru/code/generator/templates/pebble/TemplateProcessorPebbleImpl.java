package com.brotru.code.generator.templates.pebble;

import com.brotru.code.generator.templates.DirectoryTemplatesMapSupplier;
import com.brotru.code.generator.logging.Log;
import com.brotru.code.generator.templates.ContextBuilder;
import com.brotru.code.generator.templates.TemplateProcessor;
import com.github.javaparser.ast.body.FieldDeclaration;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author bronek
 */
public class TemplateProcessorPebbleImpl implements TemplateProcessor {

    private final Log log;
    PebbleEngine engine;

    public TemplateProcessorPebbleImpl(Log log) {
        this.log = log;
    }

    public TemplateProcessorPebbleImpl initialize(final Path templateRoot) throws IOException {
        return initialize(new DirectoryTemplatesMapSupplier(templateRoot).get());
    }

    @Override
    public TemplateProcessorPebbleImpl initialize(Map.Entry<String, String>... templates) {
        TemplateProcessor.super.initialize(templates);
        return this;
    }

    @Override
    public TemplateProcessorPebbleImpl initialize(final Map<String, String> templatesMap) {
        engine = new PebbleEngine.Builder()
                .loader(new MemoryMapTemplateLoader(templatesMap))
                .cacheActive(false)
                .build();
        log.info("Initialized with map: " + templatesMap);
        return this;
    }

    @Override
    public String evaluate(FieldDeclaration field, String templateName) throws IOException {
        Objects.requireNonNull(engine, "Enginge is null, was initialize() method called ?");

        return evaluate(templateName,
                new ContextBuilder(log)
                        .field(field)
                        .build()
        );
    }

    public String evaluate(String templateName, Map<String, Object> context) throws IOException {
        PebbleTemplate template = engine.getTemplate(templateName);

        StringWriter writer = new StringWriter();
        template.evaluate(writer, context);

        return writer.toString();
    }

}
