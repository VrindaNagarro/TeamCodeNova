package com.nagarro.driven.utils;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestReportLogger {

    private static final Logger log = LogManager.getLogger(TestReportLogger.class);
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void info(String message) {
        log.info(message);
        if (extentTest.get() != null) extentTest.get().info(message);
    }

    public static void pass(String message) {
        log.info("✅ " + message);
        if (extentTest.get() != null) extentTest.get().pass("✅ " + message);
    }

    public static void fail(String message) {
        log.error("❌ " + message);
        if (extentTest.get() != null) extentTest.get().fail("❌ " + message);
    }

    public static void warn(String message) {
        log.warn("⚠️ " + message);
        if (extentTest.get() != null) extentTest.get().warning("⚠️ " + message);
    }

    // Optional: clear ThreadLocal after test finishes
    public static void remove() {
        extentTest.remove();
    }
}
