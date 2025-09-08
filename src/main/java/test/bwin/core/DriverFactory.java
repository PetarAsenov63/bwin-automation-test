package test.bwin.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import test.bwin.config.Config;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory
 *
 * Creates WebDriver instances based on Config (browser, headless, device viewport).
 * Applies a small implicit wait; tests should rely primarily on explicit waits.
 */
public class DriverFactory {
        public static WebDriver create(Config cfg) {
            String browser = cfg.browser();
            String device = cfg.device();
            boolean headless = cfg.headless();
            int width = cfg.mobileWidth();
            int height = cfg.mobileHeight();

            WebDriver driver;

            switch (browser.toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions ffOpts = new FirefoxOptions();
                    if (headless) ffOpts.addArguments("--headless");
                    driver = new FirefoxDriver(ffOpts);
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;

                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chOpts = new ChromeOptions();
                    if (headless) chOpts.addArguments("--headless=new");
                    chOpts.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                    driver = new ChromeDriver(chOpts);
                    break;
            }

            // set viewport (mobile or desktop)
            setViewport(driver, device, width, height);

            // set implicit wait from config
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(cfg.implicitWait()));

            return driver;
        }


        // Device viewport
        private static void setViewport(WebDriver driver, String device, int width, int height) {
            if ("mobile".equalsIgnoreCase(device)) {
                driver.manage().window().setSize(new Dimension(width, height));
            } else {
                driver.manage().window().maximize();
            }
        }
    }
