package com.nagarro.driven.pageObjects;

import com.nagarro.driven.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class loginPage extends BasePage {

    private final By usernameField = By.id("field-userName");
    private final By passwordField = By.id("field-password");
    private final By loginButton   = By.id("btn-login");

    public loginPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
    }

    public void navigateToLoginPage(String url) {
        this.driver.get(url);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
