package test.bwin;

import org.testng.Assert;
import org.testng.annotations.Test;
import test.bwin.core.TestBase;
import test.bwin.pages.LiveBettingPage;
import test.bwin.pages.components.BetslipComponent;

public class AddPickToBetslipTest extends TestBase {

    @Test(description =
            "Add one pick to Betslip and verify item + highlight")
    public void addPickToBetslip() {
        LiveBettingPage live = new LiveBettingPage(driver, waits);
        //live.acceptCookiesIfPresent();
        live.goToEventView();            // Clicks on EventView tab to open the EventView page
        live.clickFirstOutcome();        // Clicks first odd in the Match Results section

        BetslipComponent betslip = new BetslipComponent(driver, waits);
        betslip.ensureVisible();
        Assert.assertTrue(betslip.hasItem(), "Betslip should contain at least one selection.");
        Assert.assertTrue(betslip.isAnyPickHighlighted(), "Selected pick should be highlighted.");
    }
}
