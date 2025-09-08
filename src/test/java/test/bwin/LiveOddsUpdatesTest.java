package test.bwin;

import org.testng.Assert;
import org.testng.annotations.Test;
import test.bwin.core.TestBase;
import test.bwin.pages.LiveBettingPage;
import test.bwin.util.RetryAnalyzer;

import java.time.Duration;

public class LiveOddsUpdatesTest extends TestBase {

    @Test(retryAnalyzer = RetryAnalyzer.class,
    description = "Validate live odds update dynamically and show up/down indicator (no page refresh)," +
            "I added a retry analyzer for dynamic live-odds tests that may fail intermittently due to real-time updates.")

    public void oddsUpdateAndIndicators() {
        LiveBettingPage live = new LiveBettingPage(driver, waits);

        // 1) Optional cookie
        //live.acceptCookiesIfPresent();

        // 2) Go to a fast-updating tennis table sport
        live.openTableTennisTab();

        // 3) Baseline snapshot of the first visible match
        LiveBettingPage.MatchSnapshot before = live.snapshotFirstMatch();

        // 4) Wait until any price OR indicator changes on that match
        Duration timeout = Duration.ofSeconds(Math.max(60, cfg.explicitWait())); // at least 60s is practical
        boolean updated = live.waitForFirstMatchUpdate(timeout);
        Assert.assertTrue(updated, "Expected an odds update within " + timeout.getSeconds() + "s for Table Tennis.");

        // 5) Snapshot again and compare
        LiveBettingPage.MatchSnapshot after = live.snapshotFirstMatch();

        boolean anyValueChanged = false;
        boolean anyIndicatorChanged = false;
        boolean anyIndicatorUpOrDown = false;

        int size = Math.min(before.picks().size(), after.picks().size());
        for (int i = 0; i < size; i++) {
            var b = before.picks().get(i);
            var a = after.picks().get(i);

            if (!a.value().equals(b.value())) {
                anyValueChanged = true;
            }
            if ((a.indicatorClass() == null && b.indicatorClass() != null)
                    || (a.indicatorClass() != null && b.indicatorClass() == null)
                    || (a.indicatorClass() != null && !a.indicatorClass().equals(b.indicatorClass()))) {
                anyIndicatorChanged = true;
            }
            if (a.movedUp() || a.movedDown()) {
                anyIndicatorUpOrDown = true;
            }
        }

        // 6) Assertions per task
        Assert.assertTrue(anyValueChanged || anyIndicatorChanged,
                "Expected at least odds value or indicator class to change.\nBefore: " + before + "\nAfter : " + after);

        // “green for odds up / red for odds down” => expect an up/down indicator visible after change
        Assert.assertTrue(anyIndicatorUpOrDown,
                "Expected an up/down indicator (increased/decreased) after update.\nAfter snapshot: " + after);
    }
}