package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TestDataLoader {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static Map<String, Object> loadTestData() {
        try (InputStream input = TestDataLoader.class.getClassLoader()
                .getResourceAsStream("test-data.json")) {
            if (input == null) {
                throw new RuntimeException("test-data.json not found");
            }
            return mapper.readValue(input, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getTestPrompts() {
        Map<String, Object> testData = loadTestData();
        return (Map<String, Object>) testData.get("testPrompts");
    }
}