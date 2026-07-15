package com.brotru.code.generator;

import com.brotru.code.annotations.GenerateFromField;
import com.brotru.code.generator.processors.AbstractAnnotatedFieldProcessor;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author bronek 
 */
public class GenerateFromFieldProcessor extends AbstractAnnotatedFieldProcessor {

    public GenerateFromFieldProcessor() {
        super(GenerateFromField.class);
    }

    @Override
    protected void processField(FieldDeclaration field, Optional<AnnotationExpr> annotation, List<String> fragments) {
        //annotation.get().
    }

}
