package test.bwin.config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Config
 *
 * Loads configuration from classpath resource: src/test/resources/config.properties
 * and allows overriding any value via JVM system properties (-Dkey=value).
 *
 * This class centralizes configuration used by the framework (URLs, timeouts, device, etc.).
 */
public class Config {
    private final Properties properties;

    private Config(Properties properties) {
        this.properties = properties;
    }

    /**
     * Loads config.properties and returns a Config object
     */
    public static Config load() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("⚠️ Warning: Could not load config.properties, using defaults.");
        }
        return new Config(props);
    }

    /**
     * ---------- Instance methods used by TestBase ----------
     */

    public String baseUrl() {
        return properties.getProperty("base.url", "https://sports.bwin.com/en/sports/live/betting");
    }

    public String browser() {
        return properties.getProperty("browser", "chrome");
    }

    public boolean headless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public String device() {
        return System.getProperty("device",
                properties.getProperty("device", "desktop"));
    }

    public int mobileWidth() {
        return Integer.parseInt(System.getProperty("mobile.width",
                properties.getProperty("mobile.width", "375"))); // iPhone X width default
    }

    public int mobileHeight() {
        return Integer.parseInt(System.getProperty("mobile.height",
                properties.getProperty("mobile.height", "812"))); // iPhone X height default
    }
    public int explicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "10"));
    }

    public int implicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "5"));
    }
}

