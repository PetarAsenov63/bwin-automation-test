package test.bwin.core;

import test.bwin.config.Config;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class TestBase {
    protected WebDriver driver;
    protected Config cfg;
    protected Waits waits;

    /**
     * TestBase
     *
     * Common setup/teardown for all tests:
     * - Loads configuration
     * - Creates WebDriver via DriverFactory
     * - Initializes Waits helper
     * - Navigates to the target base URL
     */

    @Parameters({"baseUrl", "device"})
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        cfg = Config.load();

        // Build driver from cfg (which already reads system properties overrides)
        driver = DriverFactory.create(cfg);
        System.out.println("[DEBUG] TestBase.setUp driver = " + driver);
        DriverManager.setDriver(driver);

        // Explicit waits helper
        waits = new Waits(driver, cfg.explicitWait());

        // Robust URL resolution (sysprop > config)
        String targetUrl = System.getProperty("baseUrl");
        if (targetUrl == null || targetUrl.isBlank()) targetUrl = cfg.baseUrl();
        if (!targetUrl.startsWith("http")) {
            throw new IllegalArgumentException("Invalid base URL: " + targetUrl);
        }
        driver.get(targetUrl);
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (DriverManager.getDriver() != null) {
            DriverManager.getDriver().quit();
            DriverManager.unload();
        }
    }
}
