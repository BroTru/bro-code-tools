package com.brotru.code.generator.templates;

import com.brotru.code.generator.logging.Log;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bronek
 */
public class ContextBuilder {

    private final Log log;

    private final Map<String, Object> context = new HashMap<>();

    public ContextBuilder(Log log) {
        this.log = log;

    }

    public ContextBuilder field(FieldDeclaration field) {
        String fieldName = field.getVariable(0).getNameAsString();
        String fieldTypeShort = field.getElementType().asString();
        
        Type fieldType = field.getElementType();        
        String fieldTypeLong = fieldType.resolve().describe();

        return this;
    }

    public Map<String, Object> build() {
        return context;
    }
}
