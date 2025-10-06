package base;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AIResponseValidator {
    
    public static boolean validateResponseQuality(String response, List<String> expectedKeywords) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        
        // Check for incomplete thoughts
        if (hasIncompleteThought(response)) {
            return false;
        }
        
        // Check for broken HTML
        if (hasBrokenHTML(response)) {
            return false;
        }
        
        // Check for expected keywords
        return containsExpectedKeywords(response, expectedKeywords);
    }
    
    public static boolean validateMultilingualConsistency(String englishResponse, String arabicResponse) {
        // Basic validation that both responses are meaningful
        return !englishResponse.trim().isEmpty() && !arabicResponse.trim().isEmpty() &&
               englishResponse.length() > 10 && arabicResponse.length() > 10;
    }
    
    public static boolean isResponseHallucinated(String response, String prompt) {
        // Basic hallucination detection
        String lowerResponse = response.toLowerCase();
        String lowerPrompt = prompt.toLowerCase();
        
        // Check if response contains generic avoidance phrases
        List<String> avoidancePhrases = Arrays.asList(
            "i cannot", "i'm not able", "as an ai", "i don't have",
            "لا أستطيع", "غير قادر", "كمنظمة ذكاء اصطناعي"
        );
        
        if (avoidancePhrases.stream().anyMatch(lowerResponse::contains)) {
            return true;
        }
        
        // Check if response is completely unrelated to prompt
        return !isResponseRelevant(response, prompt);
    }
    
    public static boolean isInputSanitized(String input, String renderedOutput) {
        // Check if potentially dangerous scripts are neutralized
        return !renderedOutput.contains("<script>") && 
               !renderedOutput.contains("javascript:") &&
               !renderedOutput.equals(input); // Should be sanitized, not identical
    }
    
    private static boolean hasIncompleteThought(String response) {
        return response.endsWith("...") || 
               response.endsWith("--") ||
               countSentences(response) < 1;
    }
    
    private static boolean hasBrokenHTML(String response) {
        Pattern brokenHTML = Pattern.compile("<[^>]*$|^[^<]*>|<[^>]*(<|$)");
        return brokenHTML.matcher(response).find();
    }
    
    private static boolean containsExpectedKeywords(String response, List<String> expectedKeywords) {
        if (expectedKeywords == null || expectedKeywords.isEmpty()) {
            return true; // No keywords to check
        }
        
        String lowerResponse = response.toLowerCase();
        return expectedKeywords.stream()
                .map(String::toLowerCase)
                .anyMatch(lowerResponse::contains);
    }
    
    private static boolean isResponseRelevant(String response, String prompt) {
        // Simple relevance check
        String[] promptWords = prompt.toLowerCase().split("\\s+");
        long matchingWords = Arrays.stream(promptWords)
                .filter(word -> word.length() > 3)
                .filter(response.toLowerCase()::contains)
                .count();
        
        return matchingWords >= 2;
    }
    
    private static int countSentences(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.split("[.!?]+").length;
    }
}
