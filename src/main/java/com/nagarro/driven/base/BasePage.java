package com.nagarro.driven.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 15;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    // ==============================
    // 🔹 Core Element Interactions
    // ==============================

    protected WebElement findElement(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element not found: " + locator);
        }
    }

    protected List<WebElement> findElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            scrollIntoView(locator);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        }
    }

    protected void type(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return findElement(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return findElement(locator).isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    // ==============================
    // 🔹 Wait & Utility Methods
    // ==============================

    protected void waitForPageTitle(String title) {
        wait.until(ExpectedConditions.titleContains(title));
    }

    protected void waitForElementInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForUrlContains(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    protected void waitForJsLoad() {
        ExpectedCondition<Boolean> jsLoad = driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        wait.until(jsLoad);
    }

    protected void scrollIntoView(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void safeClick(By locator) {
        try {
            click(locator);
        } catch (StaleElementReferenceException e) {
            System.out.println("⚠️ Retrying click due to stale element: " + locator);
            click(locator);
        }
    }

    protected void waitForElementVisible(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ==============================
    // 🔹 Logging & Error Handling
    // ==============================

    protected void log(String message) {
        System.out.println("[BasePage] " + message);
    }

    protected void captureScreenshot(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            // You can attach this to ExtentReport or save locally
            log("📸 Screenshot captured for: " + name);
        } catch (Exception e) {
            log("⚠️ Failed to capture screenshot: " + e.getMessage());
        }
    }
}
