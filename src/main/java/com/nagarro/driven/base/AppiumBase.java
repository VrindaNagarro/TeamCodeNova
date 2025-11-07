package com.nagarro.driven.base;


import com.nagarro.driven.drivers.DriverManager;
import com.nagarro.driven.drivers.MobileService;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AppiumBase {

    protected AppiumDriver driver;

    @BeforeClass
    public void setup() throws Exception {
        MobileService.startService("127.0.0.1", 4723);
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
