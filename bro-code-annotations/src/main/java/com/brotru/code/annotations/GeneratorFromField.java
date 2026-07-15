package com.brotru.code.annotations;

/**
 * Generates custom code from a field.
 *
 * @author bronek
 */
public interface GeneratorFromField {
    void generate(String fieldName, Class fieldType, Parameter[] parameters);
}
