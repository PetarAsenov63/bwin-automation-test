package test.bwin.core;

import org.openqa.selenium.WebDriver;

/**
 * DriverManager
 *
 * Thread-safe holder for WebDriver using ThreadLocal.
 * Useful if later enabling parallel execution in TestNG.
 */
    public class DriverManager {
        private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();
        public static void setDriver(WebDriver driver){ TL.set(driver);}
        public static WebDriver getDriver(){ return TL.get(); }
        public static void unload(){ TL.remove(); }
}
