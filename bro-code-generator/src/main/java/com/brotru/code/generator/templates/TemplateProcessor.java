package com.brotru.code.generator.templates;

import com.brotru.code.generator.templates.pebble.TemplateProcessorPebbleImpl;
import com.github.javaparser.ast.body.FieldDeclaration;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Processes templates from annotations data.
 *
 * @author bronek
 */
public interface TemplateProcessor {

    default TemplateProcessor initialize(final Map.Entry<String, String>... templates) {
        final Map<String, String> templatesMap = new HashMap<>(templates.length);
        for (Map.Entry<String, String> template : templates) {
            templatesMap.put(template.getKey(), template.getValue());
        }
        initialize(templatesMap);
        return this;
    }

    TemplateProcessorPebbleImpl initialize(final Map<String, String> templatesMap);

    /**
     * Parses a field definition using the given template, evaluates the template against it.
     * 
     * @param field
     * @param templateName
     * @return
     * @throws java.io.IOException
     */
    String evaluate(FieldDeclaration field, String templateName) throws IOException;
}
