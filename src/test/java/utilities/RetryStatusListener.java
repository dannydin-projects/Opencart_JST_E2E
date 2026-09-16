package utilities;

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

public class RetryStatusListener implements ITestListener {

    // Track if test was retried
    private static final Map<String, Integer> retryCount = new HashMap<>();

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getName();

        // Check if this test was retried
        if (retryCount.containsKey(testName) && retryCount.get(testName) > 0) {
            System.out.println("✅ PASSED (with " + retryCount.get(testName) + " retries): " + testName);
            retryCount.remove(testName);  // Reset
        } else {
            System.out.println("✅ PASSED: " + testName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getName();

        // Increment retry count for this test
        retryCount.put(testName, retryCount.getOrDefault(testName, 0) + 1);

        System.out.println("❌ FAILED (Retry " + retryCount.get(testName) + "): " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onStart(org.testng.ITestContext context) {}

    @Override
    public void onFinish(org.testng.ITestContext context) {}
}