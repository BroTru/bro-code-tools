package com.brotru.code.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Basic annotation that exposes a field via getter like public method that has the same name as the field.
 * @author bronek
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Expose {
    /**
     * This value is added into the javadoc top of the generated method.
     * @return javadoc part.
     */
    String value() default ""; 
}

