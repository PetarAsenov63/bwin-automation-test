package test.bwin;

import test.bwin.core.TestBase;
import test.bwin.pages.LiveBettingPage;
import test.bwin.pages.components.AZSportsComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckSportSortingTest extends TestBase {

    @Test (description =
            "Open A–Z Sports component, select a sport, verify URL + highlighted tab")
    public void openSportFromAZ() {
        LiveBettingPage live = new LiveBettingPage(driver, waits);
        //(Optional) live.acceptCookiesIfPresent();
        AZSportsComponent az = new AZSportsComponent(driver, waits);
        az.open();
        String sport = "Table Tennis"; // pick any
        az.selectSport(sport);

        Assert.assertTrue(az.urlIndicatesSport(sport),
                "URL should indicate the selected sport. Current: " + driver.getCurrentUrl());
        Assert.assertTrue(az.isSportTabHighlighted(), "Selected sport tab should be highlighted.");
    }
}
