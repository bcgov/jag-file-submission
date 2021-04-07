package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class SubmissionHistoryPage extends BasePage {

    @Value("${SUBMISSION_HISTORY_URL:http://localhost:3000/efilinghub/submissionhistory}")
    private String submissionHistoryUrl;

    @Value("${USERNAME_KEYCLOAK:bobross}")
    private String username;

    @Value("${PASSWORD_KEYCLOAK:changeme}")
    private String password;

    private final Logger logger = LoggerFactory.getLogger(SubmissionHistoryPage.class);

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    @FindBy(className = "submission-list-container")
    private WebElement submissionList;

    @FindBy(xpath = "//input[@data-testid='package-search']")
    private WebElement packageSearchTextField;

    @FindBy(className = "search-btn")
    private WebElement searchButton;

    public void signIn() {

        this.driver.get(submissionHistoryUrl);
        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();
    }

    public boolean submissionListIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(submissionList));
        return submissionList.isDisplayed();
    }

    public int verifyTableIsNotEmpty() {
        List<WebElement> tableRows = submissionList.findElements(By.tagName("tr"));
        logger.info("Total no of rows: {}", tableRows.size());
        return tableRows.size();
    }

    public void searchByPackageNumber(String packageId) {
        packageSearchTextField.sendKeys(packageId);
        searchButton.click();

    }

    public String navigateToPackageReviewFromSearchResult() {
        wait.until(ExpectedConditions.visibilityOf(submissionList));

        List<WebElement> tableRows = submissionList.findElements(By.tagName("tr"));
        logger.info("Total no of rows: {}", tableRows.size());

        String submissionHistoryTab = this.driver.getWindowHandle();
        tableRows.get(1).findElements(By.tagName("span")).get(0).click();

        ArrayList<String> getAllTabs = new ArrayList<String>(this.driver.getWindowHandles());
        getAllTabs.remove(submissionHistoryTab);
        this.driver.switchTo().window(getAllTabs.get(0));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-id='detailsTable']")));

        return this.driver.findElement(By.tagName("h1")).getText();
    }
}
