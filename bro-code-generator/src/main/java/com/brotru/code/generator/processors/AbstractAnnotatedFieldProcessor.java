package com.brotru.code.generator.processors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author bronek
 */
public abstract class AbstractAnnotatedFieldProcessor {
    private final Class<? extends Annotation> annotationClass;

    public AbstractAnnotatedFieldProcessor(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void process(FieldDeclaration field, List<String> fragments) {
        if (field.isAnnotationPresent(annotationClass)) {
            final Optional<AnnotationExpr> annotation = field.getAnnotationByClass(annotationClass);
            processField(field, annotation, fragments);
        }
    }

    protected abstract void processField(FieldDeclaration field, Optional<AnnotationExpr> annotation, List<String> fragments);
}
