package test.bwin.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import test.bwin.core.Waits;

/**
 * BetslipComponent
 *
 * Encapsulates interactions and assertions for the betslip panel.
 * Provides simple checks for presence of items and highlighted selections.
 */
public class BetslipComponent {
    private final WebDriver driver;
    private final Waits waits;
    public BetslipComponent(WebDriver d, Waits w) { this.driver=d; this.waits=w; };

    private final By betslipRoot = By.xpath("//li[contains(@class,'ds-tab-header-item')]//div[contains(normalize-space(.),'Bet Slip')]");
    private final By betslipItem = By.xpath("(//bs-digital-single-bet-pick[contains(@class,'betslip-single-bet-pick')])[1]\n");
    private final By highlighted = By.xpath("(//span[contains(@class,'market-name')][contains(.,'Match Result')]\n" +
            "   /ancestor::*[.//ms-event-pick][1]\n" +
            "   //ms-event-pick[1]\n" +
            "     [.//div[contains(@class,'option-indicator')][contains(@class,'selected')]]\n" +
            "   //div[contains(@class,'name')])[1]");

    public void ensureVisible() { waits.visible(betslipRoot); }
    public boolean hasItem(){
        try {
            waits.visible(betslipItem);   // waits until at least one matching element is visible
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
    public boolean isAnyPickHighlighted() {
        try {
            waits.visible(highlighted);   // waits until at least one matching element is visible
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
