package com.brotru.code.generator.templates.pebble;

import com.brotru.code.generator.logging.LogJulImpl;
import com.github.javaparser.ast.body.FieldDeclaration;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author bronek
 */
public class TemplateProcessorPebbleImplTest {

    private static final Logger logger = Logger.getLogger(TemplateProcessorPebbleImplTest.class.getName());

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of initialize method, of class TemplateProcessorPebbleImpl.
     */
    @org.junit.jupiter.api.Test
    public void testInitialize_MapEntryArr() {
        logger.info("initialize");
        TemplateProcessorPebbleImpl instance = new TemplateProcessorPebbleImpl(new LogJulImpl(logger))
                .initialize();
        
        assertNotNull(instance);        
    }

    /**
     * Test of evaluate method, of class TemplateProcessorPebbleImpl.
     */
    @org.junit.jupiter.api.Test
    public void testEvaluate() throws Exception {
        logger.info("evaluate");
        FieldDeclaration field = null;
        String templateName = "key";
        TemplateProcessorPebbleImpl instance = new TemplateProcessorPebbleImpl(new LogJulImpl(logger))
                .initialize(new AbstractMap.SimpleEntry<>(templateName, "name: {{name}}"));
        String expResult = "name: John";
        
        Map<String, Object> context = new HashMap<>();
        context.put("name", "John");

        String result = instance.evaluate(templateName, context);
        assertEquals(expResult, result);

    }

}