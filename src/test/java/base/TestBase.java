package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.ConfigManager;
import utils.TestDataLoader;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Map<String, Object> testData;
    protected String currentLanguage;
    
    @BeforeMethod
    @Parameters("language")
    public void setUp(String language) {
        currentLanguage = language;
        testData = TestDataLoader.loadTestData();
        initializeDriver();
        driver.get(ConfigManager.getProperty("app.url"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    private void initializeDriver() {
        // Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Mobile emulation for responsive testing
        if (ConfigManager.getProperty("test.mobile").equals("true")) {
            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "iPhone 12");
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        
        // Common stable options
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        
        // Remove headless for debugging, add back for CI/CD
        // options.addArguments("--headless");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    public void takeScreenshot(String testName) {
        // Simple screenshot implementation
        System.out.println("Screenshot would be captured for: " + testName);
        // In real implementation, use:
        // ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }
}
