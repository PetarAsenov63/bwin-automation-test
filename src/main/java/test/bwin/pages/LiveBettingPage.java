package test.bwin.pages;

import org.openqa.selenium.*;
import test.bwin.core.Waits;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LiveBettingPage
 *
 * Page Object for the Live Betting page. Includes:
 * - Cookie banner acceptance
 * - Opening a frequently-updating sport (Table Tennis)
 * - Expanding the first event if needed
 * - Snapshotting odds & update indicators for the first match
 * - Waiting for a dynamic update without page refresh
 */
public class LiveBettingPage {
    private final WebDriver driver;
    private final Waits waits;

    public LiveBettingPage(WebDriver driver, Waits waits) {
        this.driver = driver;
        this.waits = waits;
    }

    // --- Locators (kept simple; adjust if needed) ---
    private final By cookieAccept = By.cssSelector(".button[id*='onetrust-accept']");
    private final By eventViewTab = By.cssSelector(".ds-tab-horizontal[id*=ds-tab-id-1-2]");
    private final By firstOutcome = By.xpath(
            "(//span[contains(@class,'market-name')][contains(.,'Match Result')]\n" +
                    "   /ancestor::*[.//ms-event-pick][1]\n" +
                    "   //ms-event-pick[1]//div[contains(@class,'name')])[1]");
    private final By tableTennisTab = By.xpath("//li[contains(@class,'ds-tab-header-item')]//span[contains(normalize-space(.),'Table Tennis')]");
    private final By firstLocatedMatchOdds = By.xpath(
            "(   //ms-event-pick[not(contains(@class,'disabled'))]     " +
                    "[.//div[contains(@class,'option-value')][string(number(normalize-space()))!='NaN']]     " +
                    "[.//div[contains(@class,'option-indicator')][contains(@class,'increased') or contains(@class,'decreased')]] )[1] " +
                    "/ancestor::*[self::ms-regular-option-group or self::ms-period-option-group or contains(@class,'option-group')][1]");
    private final By oddsValue = By.xpath(".//div[contains(@class,'option-value')]");
    private final By oddsIndicator = By.xpath(".//div[contains(@class,'option-indicator')]");

    public void acceptCookiesIfPresent() {
        try {
            driver.findElement(cookieAccept).click();
        } catch (NoSuchElementException ignored) {
        }
    }

    public void goToEventView() {
        waits.clickable(eventViewTab).click();
    }

    /**
     * Clicks the first visible outcome button in Event View.
     */
    public void clickFirstOutcome() {
        waits.clickable(firstOutcome).click();
    }

    /**
     * Clicks the Table Tennis tab on LiveBetting page
     */
    public void openTableTennisTab() {
        waits.clickable(tableTennisTab).click();
    }

    /**
     * --- Snapshot model ---
     */
    public record PickSnapshot(String value, String indicatorClass) {
        public boolean movedUp() {
            return indicatorClass != null && indicatorClass.contains("increased");
        }

        public boolean movedDown() {
            return indicatorClass != null && indicatorClass.contains("decreased");
        }
        @Override
        public String toString() {
            String dir = movedUp() ? "↑" : movedDown() ? "↓" : "-";
            return String.format("%s %s", value, dir);
        }
    }

    public record MatchSnapshot(List<PickSnapshot> picks) {
        @Override
        public String toString() {
            return "MatchSnapshot" + picks;
        }
    }

    /**
     * --- Public API: snapshot + wait ---
     */
    public MatchSnapshot snapshotFirstMatch() {
        WebElement match = waits.visible(firstLocatedMatchOdds);
        //List<WebElement> picks = match.findElements(firstLocatedMatchOdds);
        List<WebElement> picks = match.findElements(By.tagName("ms-event-pick"));
        List<PickSnapshot> out = new ArrayList<>(picks.size());
        for (WebElement p : picks) {
            String val = p.findElement(oddsValue).getText().trim().replace(',', '.');
            String ind = p.findElement(oddsIndicator).getAttribute("class");
            out.add(new PickSnapshot(val, ind));
        }
        return new MatchSnapshot(out);
    }

    /**
     * // --- Wait until any odds value or indicator changes ---
     */
    public boolean waitForFirstMatchUpdate(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        MatchSnapshot baseline = null;

        while (System.currentTimeMillis() < end) {
            try {
                MatchSnapshot now = snapshotFirstMatch();
                if (baseline == null) {
                    baseline = now;
                } else if (now.picks().size() != baseline.picks().size()) {
                    return true;
                    // structure changed
                } else {
                    for (int i = 0; i < now.picks().size(); i++) {
                        boolean valueChanged = !now.picks().get(i).value().equals(baseline.picks().get(i).value());
                        boolean indChanged = !Objects.equals(now.picks().get(i).indicatorClass(),
                                baseline.picks().get(i).indicatorClass());
                        if (valueChanged || indChanged) return true;
                    }
                }
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                // DOM re-render; just retry
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
}


