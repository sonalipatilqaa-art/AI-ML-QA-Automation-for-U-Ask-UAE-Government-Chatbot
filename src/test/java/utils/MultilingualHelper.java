package utils;

import java.util.List;
import java.util.Map;

public class MultilingualHelper {
    
    public static boolean isRTL(String language) {
        return "ar".equals(language);
    }
    
    public static String getExpectedDirection(String language) {
        return isRTL(language) ? "rtl" : "ltr";
    }
    
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getCommonQueries() {
        Map<String, Object> testPrompts = TestDataLoader.getTestPrompts();
        return (List<Map<String, Object>>) testPrompts.get("commonQueries");
    }
    
    public static String getPromptForLanguage(Map<String, Object> query, String language) {
        return language.equals("en") ? 
            (String) query.get("en") : (String) query.get("ar");
    }
    
    @SuppressWarnings("unchecked")
    public static List<String> getExpectedKeywords(Map<String, Object> query, String language) {
        String key = language.equals("en") ? "expectedKeywords" : "arExpectedKeywords";
        return (List<String>) query.get(key);
    }
}