package com.brotru.code.generator.templates;

import com.github.javaparser.ast.body.FieldDeclaration;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Processes templates from annotations data.
 *
 * @author bronek
 */
public interface TemplateProcessor {

    /**
     * Loads templates from the given template root path.
     * 
     * @param templateRoot path of the root directory with templates
     * @return this (fluent API)
     * @throws java.io.IOException
     */
    TemplateProcessor initialize(final Path templateRoot) throws IOException;

    /**
     * Parses a field definition using the given template, evaluates the template against it.
     * 
     * @param field
     * @param templateName
     * @return
     */
    String evaluate(FieldDeclaration field, String templateName) throws IOException;
}
