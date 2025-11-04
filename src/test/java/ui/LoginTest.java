package ui;

import com.nagarro.driven.base.BaseTest;
import com.nagarro.driven.config.ConfigManager;
import org.testng.Assert;
import com.nagarro.driven.pageObjects.loginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    public loginPage loginpage;

    @BeforeMethod
    public void setUpPageObjects() {
        loginpage = new loginPage(driver);
    }

    @Test
    public void verifyLoginPageTitle() {
        String baseUrl = System.getenv("BASE_URL");
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = ConfigManager.get("base.url");
            username=ConfigManager.get("base.username");
            password=ConfigManager.get("base.password");
        }
        loginpage.navigateToLoginPage(baseUrl);
        loginpage.login(username,password);
    }
}
