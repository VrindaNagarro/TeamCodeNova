package com.nagarro.driven.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.nagarro.driven.utils.TestReportLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        TestReportLogger.setExtentTest(extentTest); // page objects can log
        extentTest.log(Status.INFO, "🚀 Starting test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("✅ Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail("❌ Test failed: " + result.getThrowable());

        // Capture screenshot if WebDriver available
        Object testClass = result.getInstance();
        try {
            Field driverField = testClass.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            WebDriver driver = (WebDriver) driverField.get(testClass);
            String screenshotPath = takeScreenshot(driver, result.getMethod().getMethodName());
            test.get().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            test.get().warning("⚠️ Screenshot not captured: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("⚠️ Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String takeScreenshot(WebDriver driver, String testName) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String folderPath = "reports/screenshots/";
        Files.createDirectories(Paths.get(folderPath));
        String path = folderPath + testName + ".png";
        Files.copy(src.toPath(), Paths.get(path));
        return path;
    }
}
