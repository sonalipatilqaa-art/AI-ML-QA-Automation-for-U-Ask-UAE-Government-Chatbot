package tests;

import base.AIResponseValidator;
import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigManager;
import utils.MultilingualHelper;

import java.util.List;
import java.util.Map;

public class AIResponseTests extends TestBase {
    
    private final By CHAT_INPUT = By.cssSelector(ConfigManager.getProperty("chat.input.selector"));
    private final By SEND_BUTTON = By.cssSelector(ConfigManager.getProperty("chat.send.selector"));
    private final By AI_RESPONSE = By.cssSelector(ConfigManager.getProperty("chat.response.selector"));
    private final By LOADING_INDICATOR = By.cssSelector(ConfigManager.getProperty("chat.loading.selector"));
    
    @Test
    public void testCommonQueriesResponseQuality() {
        List<Map<String, Object>> commonQueries = MultilingualHelper.getCommonQueries();
        
        for (Map<String, Object> query : commonQueries) {
            String prompt = MultilingualHelper.getPromptForLanguage(query, currentLanguage);
            List<String> expectedKeywords = MultilingualHelper.getExpectedKeywords(query, currentLanguage);
            
            System.out.println("Testing query: " + prompt);
            
            WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
            chatInput.sendKeys(prompt);
            
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
            sendButton.click();
            
            // Wait for response
            waitForResponse();
            
            // Get the latest response
            List<WebElement> responses = driver.findElements(AI_RESPONSE);
            if (!responses.isEmpty()) {
                WebElement latestResponse = responses.get(responses.size() - 1);
                String responseText = latestResponse.getText();
                
                System.out.println("Response length: " + responseText.length());
                System.out.println("First 200 chars: " + responseText.substring(0, Math.min(200, responseText.length())));
                
                // Validate response quality
                boolean isValidResponse = AIResponseValidator.validateResponseQuality(
                    responseText, expectedKeywords
                );
                
                Assert.assertTrue(isValidResponse, 
                    String.format("Response for '%s' should be valid and contain relevant keywords", prompt));
                
                // Check for hallucinations
                boolean isHallucinated = AIResponseValidator.isResponseHallucinated(responseText, prompt);
                Assert.assertFalse(isHallucinated,
                    String.format("Response for '%s' should not be hallucinated", prompt));
            } else {
                System.out.println("No response captured for: " + prompt);
            }
            
            // Wait before next query
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Test
    public void testResponseFormatting() {
        String testPrompt = "Explain artificial intelligence in simple terms";
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(testPrompt);
        
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        waitForResponse();
        
        List<WebElement> responses = driver.findElements(AI_RESPONSE);
        if (!responses.isEmpty()) {
            WebElement latestResponse = responses.get(responses.size() - 1);
            String responseText = latestResponse.getText();
            
            // Check for clean formatting
            Assert.assertFalse(responseText.contains("<<") || responseText.contains(">>"), 
                "Response should not contain broken formatting");
            Assert.assertFalse(responseText.endsWith("..."), 
                "Response should not end with ellipsis (incomplete thought)");
            Assert.assertTrue(responseText.length() > 50, 
                "Response should be substantial in length");
        }
    }
    
    @Test
    public void testResponseTime() {
        @SuppressWarnings("unchecked")
        Map<String, Object> validationRules = (Map<String, Object>) testData.get("validationRules");
        long maxResponseTime = ((Number) validationRules.get("maxResponseTime")).longValue();
        
        String testPrompt = "Hello, tell me a short fact";
        long startTime = System.currentTimeMillis();
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(testPrompt);
        
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        // Wait for any response element to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(AI_RESPONSE));
        long responseTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Response time: " + responseTime + "ms");
        
        Assert.assertTrue(responseTime <= maxResponseTime, 
            String.format("Response time should be under %d ms, was %d ms", maxResponseTime, responseTime));
    }
    
    @Test
    public void testFallbackMessages() {
        // Test with gibberish input
        String problematicPrompt = "asdfghjklqwertyuiop12345%%%";
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(problematicPrompt);
        
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        waitForResponse();
        
        List<WebElement> responses = driver.findElements(AI_RESPONSE);
        if (!responses.isEmpty()) {
            WebElement latestResponse = responses.get(responses.size() - 1);
            String responseText = latestResponse.getText().toLowerCase();
            
            // ChatGPT usually tries to make sense of any input, so we check for coherent response
            boolean hasReasonableResponse = responseText.length() > 20 && 
                                          !responseText.contains("error") &&
                                          !responseText.contains("invalid");
            
            Assert.assertTrue(hasReasonableResponse, 
                "Should provide a reasonable response even for unclear input");
        }
    }
    
    private void waitForResponse() {
        // Wait for loading to complete and response to appear
        try {
            // Wait for loading indicator if present
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(LOADING_INDICATOR));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_INDICATOR));
            } catch (Exception e) {
                // Loading indicator not found, continue
            }
            
            // Wait for new response element
            Thread.sleep(5000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
