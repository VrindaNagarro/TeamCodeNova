package com.nagarro.driven.pageObjects;

import com.nagarro.driven.config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WikipediaPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public WikipediaPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // default wait
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ---------- Locators ----------
    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_skip_button")
    private WebElement skipButton;

    @AndroidFindBy(id = "org.wikipedia:id/nav_more_container")
    private WebElement options;

    @AndroidFindBy(id = "org.wikipedia:id/main_drawer_login_button")
    private WebElement loginTab;

    @AndroidFindBy(id = "org.wikipedia:id/create_account_login_button")
    private WebElement createAccountLoginTab;

    @AndroidFindBy(xpath = "//*[@text='Username']")
    private WebElement usernameField;

    @AndroidFindBy(xpath = "//*[@text='Password']")
    private WebElement passwordField;

    @AndroidFindBy(id = "org.wikipedia:id/login_button")
    private WebElement submitButton;

    @AndroidFindBy(xpath = "//*[@text='NO THANKS']")
    private WebElement noThanksButton;

    @AndroidFindBy(xpath = "//*[@text='LOG OUT']")
    private WebElement logoutButton;

    @AndroidFindBy(id = "org.wikipedia:id/dialog_checkbox")
    private WebElement dialogCheckBox;

    // ---------- Actions ----------

    public void skipOnboarding() {
        if (isElementDisplayed(skipButton)) {
            waitUntilVisible(skipButton);
            skipButton.click();
        }
    }

    public void openOptions() {
        waitUntilVisible(options);
        options.click();
    }

    public void goToLogin() {
        waitUntilVisible(loginTab);
        loginTab.click();
    }

    public void switchToCreateAccountLogin() {
        waitUntilVisible(createAccountLoginTab);
        createAccountLoginTab.click();
    }

    public void enterUsername() {
        waitUntilVisible(usernameField);
        String username = System.getenv("MOB_USERNAME");
        if (username == null || username.isEmpty()) {
            username = ConfigManager.get("mobile.username");
        }
        usernameField.sendKeys(username);
    }

    public void enterPassword() {
        waitUntilVisible(passwordField);
        String password = System.getenv("MOB_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = ConfigManager.get("mobile.password");
        }
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        waitUntilVisible(submitButton);
        submitButton.click();
    }

    public void declineSavePassword() {
        if (isElementDisplayed(noThanksButton)) {
            waitUntilVisible(noThanksButton);
            noThanksButton.click();
        }
    }

    public void clickLogout() {
        waitUntilVisible(logoutButton);
        logoutButton.click();
    }

    public void checkDialog() {
        waitUntilVisible(dialogCheckBox);
        dialogCheckBox.click();
    }

    // ---------- Utility Methods ----------

    private void waitUntilVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
