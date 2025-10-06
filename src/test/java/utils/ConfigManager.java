package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    
    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getAppUrl() {
        return getProperty("app.url");
    }
    
    public static boolean isMobileTesting() {
        return Boolean.parseBoolean(getProperty("test.mobile"));
    }
    
    public static String getBrowser() {
        return getProperty("browser");
    }
}