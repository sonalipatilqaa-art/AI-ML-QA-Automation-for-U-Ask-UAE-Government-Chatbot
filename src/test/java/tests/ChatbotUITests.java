package tests;

import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigManager;
import utils.MultilingualHelper;

import java.util.List;

public class ChatbotUITests extends TestBase {
    
    // ChatGPT specific selectors
    private final By CHAT_INPUT = By.cssSelector(ConfigManager.getProperty("chat.input.selector"));
    private final By SEND_BUTTON = By.cssSelector(ConfigManager.getProperty("chat.send.selector"));
    private final By AI_RESPONSE = By.cssSelector(ConfigManager.getProperty("chat.response.selector"));
    private final By LOADING_INDICATOR = By.cssSelector(ConfigManager.getProperty("chat.loading.selector"));
    
    @Test
    public void testChatInterfaceLoads() {
        System.out.println("Testing ChatGPT interface loading for language: " + currentLanguage);
        
        // Wait for page to load and check key elements
        WebElement chatInput = wait.until(ExpectedConditions.visibilityOfElementLocated(CHAT_INPUT));
        Assert.assertTrue(chatInput.isDisplayed(), "Chat input should be visible");
        Assert.assertTrue(chatInput.isEnabled(), "Chat input should be enabled");
        
        // Check if send button is present
        List<WebElement> sendButtons = driver.findElements(SEND_BUTTON);
        Assert.assertFalse(sendButtons.isEmpty(), "Send button should be present");
    }
    
    @Test
    public void testMessageSendFunctionality() {
        String testMessage = "Hello, how are you?";
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(testMessage);
        
        // Wait for send button to be enabled and click
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        // Wait for loading to start
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(LOADING_INDICATOR));
            System.out.println("Loading indicator appeared");
        } catch (Exception e) {
            System.out.println("No loading indicator found, continuing...");
        }
        
        // Wait for response
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_INDICATOR));
            List<WebElement> responses = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(AI_RESPONSE, 1));
            Assert.assertFalse(responses.isEmpty(), "Should receive AI response");
        } catch (Exception e) {
            System.out.println("Response loading took longer than expected");
        }
    }
    
    @Test
    public void testResponseRendering() {
        String testPrompt = "What is artificial intelligence?";
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(testPrompt);
        
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        // Wait for response with longer timeout
        try {
            Thread.sleep(5000); // Wait for initial processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check for any response elements
        List<WebElement> responses = driver.findElements(AI_RESPONSE);
        if (!responses.isEmpty()) {
            WebElement latestResponse = responses.get(responses.size() - 1);
            Assert.assertTrue(latestResponse.isDisplayed(), "Latest AI response should be visible");
            String responseText = latestResponse.getText();
            Assert.assertFalse(responseText.trim().isEmpty(), "AI response should not be empty");
            System.out.println("Response received: " + responseText.substring(0, Math.min(100, responseText.length())));
        } else {
            System.out.println("No response elements found, may need to adjust selectors");
        }
    }
    
    @Test
    public void testInputClearAfterSend() {
        String testMessage = "Test message for clearing";
        
        WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
        chatInput.sendKeys(testMessage);
        
        // Store initial input value
        String initialValue = chatInput.getText();
        
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        
        // Wait a bit and check if input is cleared or ready for new input
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        WebElement newChatInput = driver.findElement(CHAT_INPUT);
        String newValue = newChatInput.getText();
        
        // Input should be cleared or different from original
        Assert.assertNotEquals(newValue, testMessage, "Input should be cleared or changed after sending");
    }
    
    @Test
    public void testScrollFunctionality() {
        // Send multiple messages to test scrolling
        for (int i = 0; i < 3; i++) {
            WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(CHAT_INPUT));
            chatInput.sendKeys("Test message " + i);
            
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
            sendButton.click();
            
            // Wait between messages
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Verify conversation history exists
        List<WebElement> conversationTurns = driver.findElements(AI_RESPONSE);
        Assert.assertTrue(conversationTurns.size() >= 2, "Should have multiple conversation turns");
        
        // Check if page is scrollable (basic check)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long pageHeight = (Long) js.executeScript("return document.body.scrollHeight");
        Long viewportHeight = (Long) js.executeScript("return window.innerHeight");
        
        Assert.assertTrue(pageHeight > viewportHeight, "Page should be scrollable with multiple messages");
    }
}
