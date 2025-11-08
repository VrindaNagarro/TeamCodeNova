package ui;

import com.nagarro.driven.base.BaseTest;
import com.nagarro.driven.pageObjects.EspnCricinfoPage;
import com.nagarro.driven.config.ConfigManager;
import com.nagarro.driven.pageObjects.loginPage;
import com.nagarro.driven.utils.ExcelReader;
import com.nagarro.driven.utils.TestReportLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EspnCricinfoTest extends BaseTest {
    EspnCricinfoPage espn;
    @BeforeMethod
    public void setUpPageObjects() {
        espn = new EspnCricinfoPage(driver);
    }

    @Test
    public void verifyEspnCricinfoMenus() {
        String inputPath = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "main"
                + File.separator + "resources"
                + File.separator + "TestData"
                + File.separator + "Challenge A1 - Source File.xlsx";

        String outputPath = "reports/EspnCricinfoResults.xlsx";

        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = ConfigManager.get("base.url");
        }

        espn.navigateToLoginPage(baseUrl);
        List<Map<String, String>> testData = espn.getDataFromExcel(inputPath);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = espn.createResultExcel(workbook);
        espn.verifyAllMenuLinks(sheet, testData);
        espn.saveExcel(workbook, outputPath);
        System.out.println("✅ Menu link validation completed. Results saved at: " + outputPath);
    }

    @Test
    public void verifyTeamStatsTest() throws InterruptedException {
        EspnCricinfoPage espn = new EspnCricinfoPage(driver);
        String inputPath = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "main"
                + File.separator + "resources"
                + File.separator + "TestData"
                + File.separator + "Challenge A2 - Source File.xlsx";
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = ConfigManager.get("base.url");
        }

        espn.navigateToLoginPage(baseUrl);

        ExcelReader reader = new ExcelReader(inputPath);
        List<Map<String, String>> excelData = reader.getData("Teams");

        for (Map<String, String> row : excelData) {
            String team = row.getOrDefault("Team", "").trim();
            String format = row.getOrDefault("Match Format", "").trim();

            TestReportLogger.info("=== Processing Team: " + team + " | Format: " + format + " ===");

            try {
                espn.closePopupIfVisible();
                espn.hoverOnTeamsMenu();
                Thread.sleep(1500);
                espn.clickTeam(team);
                Thread.sleep(2000);
                espn.clickStatsTab(team);
                Thread.sleep(2000);
                espn.verifyStatsHeaders();
                espn.clickViewFullList(format);
                TestReportLogger.info("✅ Verification completed for team: " + team);

                driver.navigate().to(baseUrl);
                Thread.sleep(2000);

            } catch (Exception e) {
                TestReportLogger.info("❌ Test failed for Team: " + team + " — " + e.getMessage());
            }
        }
    }
}
