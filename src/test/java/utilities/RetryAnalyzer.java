package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = (Logger) LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY = 3;

    @Override
    public boolean retry(ITestResult result) {
        // Only retry if test failed AND retry count is less than max
        if (result.getStatus() == ITestResult.FAILURE && retryCount < MAX_RETRY) {
            retryCount++;
            logger.warn("🔄 RETRY ATTEMPT #" + retryCount + " of " + MAX_RETRY + ": " + result.getName());
            return true;  // Retry the test
        }
        
        retryCount = 0;  // Reset for next test
        return false;    // Stop retrying (passed or max retries exhausted)
    }
}
