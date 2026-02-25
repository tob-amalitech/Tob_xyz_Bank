package com.globalbanktests.tests.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility to load test data from JSON files on the test classpath.
 * Data is loaded once and cached for all tests.
 */
public final class TestDataLoader {

    private static final String DEFAULT_DATA_FILE = "test-data/global-bank-tests.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonNode ROOT;

    static {
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream(DEFAULT_DATA_FILE)) {
            if (is == null) {
                throw new IllegalStateException("Test data file not found on classpath: " + DEFAULT_DATA_FILE);
            }
            ROOT = MAPPER.readTree(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from " + DEFAULT_DATA_FILE, e);
        }
    }

    private TestDataLoader() {
        // utility
    }

    /**
     * Returns the root JSON node for the default test data file.
     */
    public static JsonNode data() {
        return ROOT;
    }
}

