package mobile;


import com.nagarro.driven.base.AppiumBase;
import com.nagarro.driven.pageObjects.WikipediaPage;
import org.testng.annotations.Test;

public class WikipediaTest extends AppiumBase {

    @Test
    public void searchInWikipedia() throws InterruptedException {
        WikipediaPage wikiPage = new WikipediaPage(driver);
        wikiPage.skipOnboarding();
        wikiPage.openOptions();
        wikiPage.goToLogin();
        wikiPage.switchToCreateAccountLogin();
        wikiPage.enterUsername();
        wikiPage.enterPassword();
        wikiPage.clickLogin();
        wikiPage.declineSavePassword();
    }
}