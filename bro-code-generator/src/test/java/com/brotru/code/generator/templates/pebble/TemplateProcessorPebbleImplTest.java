package com.brotru.code.generator.templates.pebble;

import com.brotru.code.generator.logging.LogJulImpl;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testInitialize_MapEntryArr() {
        logger.info("initialize start");
        TemplateProcessorPebbleImpl instance = new TemplateProcessorPebbleImpl(new LogJulImpl(logger))
                .initialize();

        assertNotNull(instance.engine);
        logger.info("initialize end");
    }

    @Test
    public void testEvaluateWithContext() throws Exception {
        logger.info("evaluate start");
        String templateName = "template-1";
        TemplateProcessorPebbleImpl instance = new TemplateProcessorPebbleImpl(new LogJulImpl(logger))
                .initialize(new AbstractMap.SimpleEntry<>(templateName, "name: {{name}}"));
        String expResult = "name: John";

        Map<String, Object> context = new HashMap<>();
        context.put("name", "John");

        String result = instance.evaluate(templateName, context);
        assertEquals(expResult, result);
        logger.info("evaluate end");
    }

}