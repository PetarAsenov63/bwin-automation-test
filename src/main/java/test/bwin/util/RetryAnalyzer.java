package test.bwin.util;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer
 *
 * This class is used to automatically re-run a failed test case.
 * It is particularly useful for tests against dynamic, real-time systems
 * (such as live odds updates on Bwin) where occasional flakiness may occur.
 *
 * By default, it will retry a failed test **once**.
 *
 * Usage:
 *  @Test(retryAnalyzer = RetryAnalyzer.class)
 *  public void myFlakyTest() {
 *      ...
 *  }
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true; // retry the test
        }
        return false;
    }
}
