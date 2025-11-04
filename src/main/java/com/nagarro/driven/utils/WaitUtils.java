package com.nagarro.driven.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class WaitUtils {

    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSec) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementClickable(WebDriver driver, By locator, int timeoutSec) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class);

        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForPageTitle(WebDriver driver, String title, int timeoutSec) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(Exception.class);

        return wait.until(d -> d.getTitle().contains(title));
    }
}
