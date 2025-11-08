package ui;

import com.nagarro.driven.base.BaseTest;
import com.nagarro.driven.pageObjects.EspnCricinfoPage;
import com.nagarro.driven.config.ConfigManager;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class EspnCricinfoTest extends BaseTest {

    @Test
    public void verifyEspnCricinfoMenus() {
        String inputPath = "C:\\Users\\vrindasharma\\Downloads\\AutomATAhon\\src\\main\\resources\\TestData\\Challenge A1 - Source File.xlsx";
        String outputPath = "reports/EspnCricinfoResults.xlsx";

        EspnCricinfoPage espn = new EspnCricinfoPage(driver);
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = ConfigManager.get("base.url");
        }

        espn.navigateToLoginPage(baseUrl);
        List<Map<String, String>> testData = espn.getDataFromExcel(inputPath);

        // Step 3: Create output workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = espn.createResultExcel(workbook);

        // Step 4: Verify all menus and submenus
        espn.verifyAllMenuLinks(sheet, testData);

        // Step 5: Save result file
        espn.saveExcel(workbook, outputPath);

        System.out.println("✅ Menu link validation completed. Results saved at: " + outputPath);
    }
}
