package com.maveric.core.testng.listeners;

import com.maveric.core.cucumber.reporter.CucumberReporter;
import com.maveric.core.testng.reporter.CustomReporter;
import com.maveric.core.utils.reporter.Report;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import static com.maveric.core.utils.GenericUtils.getTimeStamp;

public class ReportListener implements ITestListener, ISuiteListener, IInvokedMethodListener {
    private static final Logger logger = LogManager.getLogger();
    public static String reportFolder;
    public static String screenshotFolder;
    public String includedGroups;
    public String excludedGroups;
    public static boolean isCucumber = false;

    @Override
    public void onStart(ISuite suite) {
        reportFolder = "./reports/test-output-" + getTimeStamp();
        File reportsFolder = new File(reportFolder);
        File screenShotsFolder = new File(reportsFolder + "/screenshots");
        reportsFolder.mkdirs();

        logger.info("reports folder created successfully :" + reportFolder);
        screenShotsFolder.mkdirs();
        screenshotFolder = reportsFolder + "/screenshots";
        CustomReporter.createCustomReport(reportFolder + "/CustomReport.html");
        try {
            FileUtils.copyFile(new File("./lib/MavericLogo.png"), new File(screenshotFolder + "/MavericLogo.png"));
            FileUtils.copyFile(new File("./lib/Background.png"), new File(screenshotFolder + "/Background.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        includedGroups = StringUtils.join(suite.getXmlSuite().getIncludedGroups(), ",");
        excludedGroups = StringUtils.join(suite.getXmlSuite().getExcludedGroups(), ",");

    }


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result, ITestContext context) {

        if (method.isTestMethod())
            if (!isCucumber) {
                String testExecTime = convert(result.getEndMillis() - result.getStartMillis());
                if (!result.isSuccess()) {
                    StringWriter sw = new StringWriter();
                    result.getThrowable().printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Report.log(exceptionAsString);
                }
                String logs = String.join("<br>", Reporter.getOutput(result));
                String testName = result.getMethod().getMethodName();
                if (result.isSuccess()) {
                    CustomReporter.appendPass(logs, testName, testExecTime);
                } else {
                    int currentReteryCount = RetryAnalyzer.getCount();
                    if (currentReteryCount > 0) {
                        testName = testName + "_Retry_" + currentReteryCount;
                    }
                    CustomReporter.appendFail(logs, testName, testExecTime);
                }

            }
    }


    public String convert(long miliSeconds) {
        int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
        return String.format("%02d:%02d:%02d", hrs, min, sec);
    }


    @Override
    public void onFinish(ITestContext ctx) {
        long start = ctx.getStartDate().getTime();
        long end = ctx.getEndDate().getTime();
        String executionTime = convert(end - start);
        String endDate = ctx.getEndDate().toString();
        if (!isCucumber) {
            int passedCount = ctx.getPassedTests().size();
            int failedCount = ctx.getFailedTests().size();
            CustomReporter.updateValues(passedCount, failedCount, endDate, executionTime, includedGroups + "\n" + excludedGroups);
        } else {

            CucumberReporter.generateCucumberReport();
            CustomReporter.updateValues(CucumberReporter.passedFeatures,
                    CucumberReporter.totalFeatures - CucumberReporter.passedFeatures, endDate, executionTime, CucumberReporter.featureTags);

        }
    }


}
