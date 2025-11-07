package com.nagarro.driven.base;


import com.nagarro.driven.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AppiumBase {

    protected AppiumDriver driver;

    @BeforeClass
    public void setup() throws Exception {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
