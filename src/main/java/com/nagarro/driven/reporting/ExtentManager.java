package com.nagarro.driven.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentManager.class) {
                if (extent == null) {
                    // Report path
                    String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
                    File reportFile = new File(reportPath);

                    // ✅ Delete previous report (if exists)
                    if (reportFile.exists()) {
                        reportFile.delete();
                    }

                    // Create a new Spark reporter
                    ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
                    spark.config().setDocumentTitle("Automation Execution Report");
                    spark.config().setReportName("Test Execution Results");
                    // ✅ Ensure it overwrites old report, not append
                    spark.config().setTimelineEnabled(false);

                    extent = new ExtentReports();
                    extent.attachReporter(spark);

                    extent.setSystemInfo("Framework", "AutomATAhon");
                    extent.setSystemInfo("Environment", System.getProperty("environment", "dev"));
                    extent.setSystemInfo("OS", System.getProperty("os.name"));
                    extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                }
            }
        }
        return extent;
    }
}
