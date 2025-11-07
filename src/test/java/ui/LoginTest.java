package ui;

import com.nagarro.driven.base.BaseTest;
import com.nagarro.driven.config.ConfigManager;
import com.nagarro.driven.pageObjects.loginPage;
import com.nagarro.driven.utils.TestReportLogger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LoginTest extends BaseTest {

    private static final Logger LOGGER = Logger.getLogger(LoginTest.class.getName());

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
        TestReportLogger.info("BaseUrl:"+baseUrl);
        TestReportLogger.info("Username: " + username);
        TestReportLogger.info("Password: " + password);
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = ConfigManager.get("base.url");
            username = ConfigManager.get("base.username");
            password = ConfigManager.get("base.password");
        }
        loginpage.navigateToLoginPage(baseUrl);
        loginpage.login(username, password);
    }
}
