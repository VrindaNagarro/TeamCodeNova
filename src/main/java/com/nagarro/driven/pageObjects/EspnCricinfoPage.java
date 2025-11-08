package com.nagarro.driven.pageObjects;

import com.nagarro.driven.base.BasePage;
import com.nagarro.driven.utils.ExcelReader;
import com.nagarro.driven.utils.TestReportLogger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Page Object for ESPN Cricinfo
 * Handles login, Excel data extraction, and link validation
 */
public class EspnCricinfoPage extends BasePage {

    private ExcelReader reader;
    private List<Map<String, String>> rows;

    Actions actions;
    private final By usernameField = By.id("field-userName");
    private final By passwordField = By.id("field-password");
    private final By loginButton = By.id("btn-login");

    public EspnCricinfoPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        actions=new Actions(driver);
    }

    /** ========================= Excel ========================= */
    public List<Map<String, String>> getDataFromExcel(String inputPath) {
        reader = new ExcelReader(inputPath);
        rows = reader.getData("ESPN_CRIC_INFO");
        return rows;
    }

    public XSSFSheet createResultExcel(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet("Results");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Menu Item");
        header.createCell(1).setCellValue("Menu Anchor Link");
        header.createCell(2).setCellValue("Menu HTTP Code");
        header.createCell(3).setCellValue("Sub-menu Item");
        header.createCell(4).setCellValue("Sub-menu Anchor Link");
        header.createCell(5).setCellValue("Sub-menu HTTP Code");
        header.createCell(6).setCellValue("Status");
        header.createCell(7).setCellValue("Failure reason if any?");
        return sheet;
    }

    public void saveExcel(XSSFWorkbook workbook, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel: " + e.getMessage());
        }
    }

    /** ========================= Login ========================= */
    public void navigateToLoginPage(String url) {
        TestReportLogger.info("Navigating to URL: " + url);
        driver.get(url);
    }

    public void login(String username, String password) {
        TestReportLogger.info("Performing login for user: " + username);
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    /** ========================= Menu Verification ========================= */
    public void verifyAllMenuLinks(XSSFSheet sheet, List<Map<String, String>> data) {
        int rowNum = 1;

        for (Map<String, String> map : data) {
            String menuItem = map.getOrDefault("Menu (Expected)", "").trim();
            String subMenuItem = map.getOrDefault("Sub-menu (Expected)", "").trim();

            Row row = sheet.createRow(rowNum++);

            try {
                // ===== Find Menu =====
                List<WebElement> menus = driver.findElements(
                        By.xpath("//a[contains(normalize-space(text()),'" + menuItem + "')]")
                );

                if (menus.isEmpty()) {
                    writeResult(row, menuItem, "", "", subMenuItem, "", "", "FAIL", "Menu not found");
                    continue;
                }

                WebElement menu = menus.get(0);
                String menuLink = menu.getAttribute("href");
                int menuStatus = getHttpStatus(menuLink);
                // ===== Find Sub-menu =====
                boolean clicked=false;
                if(clicked) {
                    try {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Not Now')]")));
                        WebElement notNowButton = driver.findElement(By.xpath("//button[contains(text(),'Not Now')]"));
                        if (notNowButton.isDisplayed()) {
                            notNowButton.click();
                            clicked=true;
                        }
                    } catch (Exception e) {
                        // Do nothing if button not found or not visible after 30 seconds
                    }
                }
                try {
                    actions.moveToElement(menu).perform();
                }catch(Exception e){
                    List<WebElement> menuitem = driver.findElements(
                            By.xpath("//a[contains(normalize-space(text()),'" + menuItem + "')]")
                    );
                    actions.moveToElement(menuitem.get(0)).perform();

                }
                try {
                    WebElement subMenus = driver.findElement(
                            By.xpath("//span[contains(text(),'" + subMenuItem + "') and @class='ds-grow']")
                    );

                    if (subMenus != null) {
                        // Get the direct parent <a> tag of this <span>
                        WebElement parentAnchor = subMenus.findElement(By.xpath("parent::a"));
                        String subLink = parentAnchor.getAttribute("href");
                        int subStatus = getHttpStatus(subLink);

                        writeResult(
                                row,
                                menuItem,
                                menuLink,
                                String.valueOf(menuStatus),
                                subMenuItem,
                                subLink,
                                String.valueOf(subStatus),
                                (menuStatus == 200 && subStatus == 200) ? "PASS" : "FAIL",
                                ""
                        );
                    } else {
                        writeResult(
                                row,
                                menuItem,
                                menuLink,
                                String.valueOf(menuStatus),
                                subMenuItem,
                                "",
                                "",
                                "FAIL",
                                "Sub-menu element not found"
                        );
                    }

                } catch (Exception e) {
                    // If element location fails — mark as FAIL
                    writeResult(
                            row,
                            menuItem,
                            menuLink,
                            String.valueOf(menuStatus),
                            subMenuItem,
                            "",
                            "",
                            "FAIL",
                            "sub-menu not found "
                    );
                }


            } catch (Exception e) {
                writeResult(row, menuItem, "", "", subMenuItem, "", "", "FAIL", e.getMessage());
            }
        }
    }

    private void writeResult(Row row, String menu, String menuLink, String menuCode,
                             String subMenu, String subLink, String subCode, String status, String reason) {
        row.createCell(0).setCellValue(menu);
        row.createCell(1).setCellValue(menuLink);
        row.createCell(2).setCellValue(menuCode);
        row.createCell(3).setCellValue(subMenu);
        row.createCell(4).setCellValue(subLink);
        row.createCell(5).setCellValue(subCode);
        row.createCell(6).setCellValue(status);
        row.createCell(7).setCellValue(reason);
    }

    /** ========================= HTTP Verification (Improved) ========================= */
    private int getHttpStatus(String url) {
        if (url == null || url.isEmpty() || url.startsWith("javascript")) return 0;

        try {
            // Use modern HttpClient with cookies & timeout
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            String cookies = buildCookieHeader();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            if (!cookies.isEmpty()) {
                requestBuilder.header("Cookie", cookies);
            }

            HttpRequest request = requestBuilder.GET().build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            int status = response.statusCode();

            // ===== Handle false 403 =====
            if (status == 403) {
                TestReportLogger.warn("403 received for " + url + " — verifying via browser navigation fallback...");
                if (isLinkAccessibleViaBrowser(url)) {
                    return 200;
                }
            }

            return status;

        } catch (Exception e) {
            return 0;
        }
    }

    /** Fallback — open in browser to confirm if actually reachable */
    private boolean isLinkAccessibleViaBrowser(String url) {
        try {
            String current = driver.getCurrentUrl();
            driver.navigate().to(url);
            String newUrl = driver.getCurrentUrl();
            boolean accessible = !newUrl.contains("error") && !newUrl.contains("404");
            driver.navigate().to(current);
            return accessible;
        } catch (Exception e) {
            return false;
        }
    }

    /** ========================= Cookies ========================= */
    private String buildCookieHeader() {
        try {
            Set<Cookie> cookies = driver.manage().getCookies();
            if (cookies == null || cookies.isEmpty()) return "";
            return cookies.stream()
                    .map(c -> c.getName() + "=" + c.getValue())
                    .collect(Collectors.joining("; "));
        } catch (Exception e) {
            return "";
        }
    }
}
