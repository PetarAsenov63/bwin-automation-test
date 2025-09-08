package test.bwin.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Waits
 *
 * Minimal explicit wait wrapper to keep test code clean and readable.
 * Centralizes timeout configuration and common wait patterns.
 */
public class Waits {
    private final WebDriverWait wait;

    public Waits(WebDriver driver, long timeoutSec) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

}