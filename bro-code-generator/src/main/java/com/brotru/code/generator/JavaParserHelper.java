package com.brotru.code.generator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.File;

/**
 *
 * @author bronek
 */
public class JavaParserHelper {

    public static void initializeTypeSolvers() {
        initializeTypeSolvers(null);
    }

    public static void initializeTypeSolvers(File sourcesDirectory) {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());

        if (sourcesDirectory != null) {
            typeSolver.add(new JavaParserTypeSolver(sourcesDirectory));
        }

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getParserConfiguration().setSymbolResolver(symbolSolver);
    }
}
