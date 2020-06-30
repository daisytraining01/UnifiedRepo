
package com.maveric.core.testng.listeners;

import com.maveric.core.config.ConfigProperties;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static AtomicInteger count;
    private static final int maxTry = ConfigProperties.MAX_RETRY_COUNT.getInt();

    public RetryAnalyzer() {
        count = new AtomicInteger(0);
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count.get() < maxTry) {
                count.incrementAndGet();
                iTestResult.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }

    public static int getCount() {
        return count.get();
    }

}


