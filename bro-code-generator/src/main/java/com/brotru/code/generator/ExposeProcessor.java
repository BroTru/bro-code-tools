package com.brotru.code.generator;

import com.brotru.code.annotations.Expose;
import com.brotru.code.generator.processors.AbstractAnnotatedFieldProcessor;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author bronek
 */
public class ExposeProcessor extends AbstractAnnotatedFieldProcessor {

    public ExposeProcessor() {
        super(Expose.class);
    }

    @Override
    protected void processField(FieldDeclaration field, Optional<AnnotationExpr> annotation, List<String> fragments) {
            final String additionalJavadoc = annotation
                    .flatMap(ann -> ann.findFirst(StringLiteralExpr.class))
                    .map(StringLiteralExpr::getValue)
                    .orElse("");
            String fieldName = field.getVariable(0).getNameAsString();
            String fieldType = field.getElementType().asString();
            String template = "    /**\n";
            if (!additionalJavadoc.isBlank()) {
                template = template + "     * " + additionalJavadoc + "\n";
            }
            template = template +
                    "     * Returns instance of {@link %1$s}.\n" +
                    "     * See: {@link #%2$s}\n" +
                    "     * @return {@link %1$s} instance from field {@link #%2$s}\n" +
                    "     */\n" +
                    "    public %1$s %2$s() { return %2$s; }\n";

            String methodBlock = String.format(template, fieldType, fieldName);
            fragments.add(methodBlock);
        
    }
}
