package com.nagarro.driven.drivers;

import com.nagarro.driven.config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

public class DriverManager {

    private static AppiumDriver driver;

    public static void initDriver() throws Exception {
        if (driver == null) {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", ConfigManager.get("platformName"));
            caps.setCapability("deviceName", ConfigManager.get("deviceName"));
            caps.setCapability("automationName", ConfigManager.get("automationName"));
            caps.setCapability("app", new File(ConfigManager.get("app")).getAbsolutePath());
            caps.setCapability("appPackage", ConfigManager.get("appPackage"));
            caps.setCapability("appActivity", ConfigManager.get("appActivity"));
            caps.setCapability("autoGrantPermissions", true);
            caps.setCapability("newCommandTimeout", 300);

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
