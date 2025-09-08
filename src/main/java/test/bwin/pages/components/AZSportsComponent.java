package test.bwin.pages.components;

import test.bwin.core.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

/**
 * AZSportsComponent
 *
 * Encapsulates the A–Z Sports navigation UI. It can be reused anywhere
 * the A–Z component appears (e.g., Live page).
 */
public class AZSportsComponent {
    private final WebDriver driver;
    private final Waits waits;

    public AZSportsComponent(WebDriver driver, Waits waits) {
        this.driver = Objects.requireNonNull(driver, "AZSportsComponent: driver is null");
        this.waits  = Objects.requireNonNull(waits,  "AZSportsComponent: waits is null");
    }

    // Trigger that opens the A–Z panel/section (adjust if needed)
    private final By azSportsTab = By.xpath("//vn-menu-item[contains(@class,'menu-item')][normalize-space()='A-Z Sports']\n");
    // Active/selected sport tab indicator (generic fallback)
    private final By selectedSport = By.xpath("//nav[contains(@id,'sports-nav')]" +
            "//div[contains(@class,'main-items')]/vn-menu-item[contains(@class,'active') or " +
            ".//*[contains(@class,'active')]]\n");

    public void open() {
        waits.clickable(azSportsTab).click();
    }

    /** Click a sport by visible text / slug in href. */
    public void selectSport(String name) {
        By link = By.xpath(
                "//a[contains(@href,'/sports/') and contains(" +
                        "translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + name.toLowerCase() + "')]"
        );
        waits.clickable(link).click();
    }

    /** Many UIs highlight the selected sport. */
    public boolean isSportTabHighlighted() {
        try {
            return driver.findElement(selectedSport).isDisplayed();
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    /** Loose URL check that the selected sport is reflected in the path. */
    public boolean urlIndicatesSport(String sportName) {
        Objects.requireNonNull(sportName, "urlIndicatesSport: sportName is null");
        String url = driver.getCurrentUrl().toLowerCase();
        String normalized = sportName.toLowerCase().replace(" ", "-");
        return url.contains(normalized)
                || url.contains(sportName.toLowerCase().replace(" ", ""))
                || url.contains(sportName.toLowerCase().replace(" ", "_"));
    }
}