package com.brotru.code.annotations;

/**
 * Allows passing custom parameters to generated code.
 *
 * @author bronek
 */
public @interface Parameter {
    String key();
    
    String stringVal() default "";
    int intVal() default 0;
    double doubleVal() default 0.0;
    boolean boolVal() default false;
}
