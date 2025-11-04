package com.nagarro.driven.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test Failed: " + result.getThrowable());

        // Attach screenshot if WebDriver is available
        Object testClass = result.getInstance();
        try {
            WebDriver driver = (WebDriver) testClass.getClass().getField("driver").get(testClass);
            String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
            test.get().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            test.get().log(Status.WARNING, "Screenshot not captured: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String captureScreenshot(WebDriver driver, String testName) throws IOException {
        String folderPath = "reports/screenshots/";
        Files.createDirectories(Paths.get(folderPath));
        String filePath = folderPath + testName + ".png";
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(), Paths.get(filePath));
        return filePath;
    }
}
