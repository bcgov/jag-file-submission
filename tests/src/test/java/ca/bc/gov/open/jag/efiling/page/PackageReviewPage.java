package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PackageReviewPage extends BasePage {

    @Value("${PACKAGE_REVIEW_URL:http://localhost:3000/efilinghub/packagereview}")
    private String packageReviewUrl;

    @Value("${USERNAME_KEYCLOAK:bobross}")
    private String username;

    @Value("${PASSWORD_KEYCLOAK:changeme}")
    private String password;

    @Value("${PACKAGE_ID:1}")
    private String packageId;

    private final Logger logger = LoggerFactory.getLogger(PackageReviewPage.class);

    @FindBy(className = "bcgov-row")
    private List<WebElement> packageDetailsRow;

    @FindBy(className = "bcgov-fill-width")
    private List<WebElement> packageKeyDetails;

    @FindBy(xpath = "//*[contains(@class,'bcgov-table-value')]/b")
    private List<WebElement> packageValueDetails;

    @FindBy(id = "controlled-tab-tab-documents")
    private WebElement documentsTab;

    @FindBy(xpath = "//*[@data-testid='btn-download-document']")
    private WebElement downloadButton;

    @FindBy(id = "controlled-tab-tab-parties")
    private WebElement partiesTab;

    @FindBy(id = "controlled-tab-tab-comments")
    private WebElement filingCommentsTab;

    @FindBy(id = "controlled-tab-tab-payment")
    private WebElement paymentStatusTab;

    @FindBy(id = "controlled-tab-tabpane-documents")
    private WebElement documentPane;

    @FindBy(id = "controlled-tab-tabpane-payment")
    private WebElement paymentPane;

    @FindBy(xpath = "//*[@data-testid='btn-view-receipt']")
    private WebElement viewReceiptButton;

    @FindBy(id = "controlled-tab-tabpane-parties")
    private WebElement partiesTable;

    @FindBy(id = "filingComments")
    private WebElement filingCommentsTextBox;

    @FindBy(xpath = "//*[@data-testid='cso-link']")
    private WebElement linkToCso;

    @FindBy(className = "bcgov-error-background")
    private WebElement alertComponent;

    @FindBy(xpath = "//*[@data-testid='btn-registry-notice']")
    private WebElement registryNoticeBtn;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    @SuppressWarnings("boxing")
	public List<String> getPackageDetails() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-test-id='detailsTable']")));

        logger.info("Getting package details values");

        List<String> packageDetails = new ArrayList<>();
        packageValueDetails.forEach(webElement -> {
            if (!webElement.isDisplayed()) {
                logger.info("WebElement is still not visible. Waiting for all rows to be present: {}", webElement.isDisplayed());
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-test-id='detailsTable'] //*[contains(@class,'bcgov-table-value')]/b")));
            }
            logger.info("Is it visible: {}", webElement.isDisplayed());
            logger.info("Is it enabled: {}", webElement.isEnabled());
            packageDetails.add(webElement.getText());
        });
        return packageDetails;
    }

    @SuppressWarnings("boxing")
    public void clickToDownloadDocument() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-testid='btn-download-document']")));

        logger.info("Download button is enabled: {}", downloadButton.isEnabled());
        logger.info("Downloaded file name is: {}", downloadButton.getText());
        downloadButton.click();
        Thread.sleep(1500L);
    }

    public void clickDocumentsTab() {
        documentsTab.click();
    }

    @SuppressWarnings("boxing")
    public List<String> getAllParties() {

        partiesTab.click();

        List<WebElement> tableRows = partiesTable.findElements(By.tagName("li"));
        logger.info("Total no of rows: {}", tableRows.size());

        List<String> columnValues = new ArrayList<>();

        for (WebElement row : tableRows) {
            List<WebElement> tableColumns = row.findElements(By.tagName("span"));

            columnValues.addAll(getColumnValues(tableColumns));
        }
        return columnValues;
    }

    private List<String> getColumnValues(List<WebElement> tableColumns) {
        List<String> columnValues = new ArrayList<>();

        for (WebElement column : tableColumns) {
            if (!(column.getText() == null) && !(column.getText().isEmpty())) {
                columnValues.add(column.getText());
                logger.info(column.getText() + " ");
            }
        }
        return columnValues;
    }

    public void clickFilingCommentsTab() {
        filingCommentsTab.click();
    }

    public void clickPaymentStatusTab() {
        paymentStatusTab.click();
    }

    public boolean verifyDocumentsPaneIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(documentPane));
        return documentPane.isDisplayed();
    }

    public boolean verifyFilingCommentsIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(filingCommentsTextBox));
        return filingCommentsTextBox.isDisplayed();
    }

    public boolean verifyPaymentStatusIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(paymentPane));
        return paymentPane.isDisplayed();
    }

    @SuppressWarnings("boxing")
    public void clickToDownloadReceipt() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-testid='btn-view-receipt']")));
        logger.info("Download button is enabled: {}", viewReceiptButton.isEnabled());

        viewReceiptButton.click();
        Thread.sleep(1500L);
    }

    public String getCsoPageUrlAndSwitchToPackageReviewPage() {
        String packageReviewTab = this.driver.getWindowHandle();
        linkToCso.click();

        ArrayList<String> getAllTabs = new ArrayList<String>(this.driver.getWindowHandles());
        getAllTabs.remove(packageReviewTab);

        this.driver.switchTo().window(getAllTabs.get(0));
        String csoTabTitle = this.driver.getCurrentUrl();
        this.driver.close();

        this.driver.switchTo().window(packageReviewTab);

        return csoTabTitle;
    }

    public String getCurrentPageTitle() {
        return this.driver.getTitle();
    }

    @SuppressWarnings("boxing")
    public void clickToDownloadRegistryNotice() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(alertComponent));

        logger.info("Registry notice alert is visible: {}", alertComponent.isDisplayed());
        logger.info("Downloaded file name is: {}", registryNoticeBtn.getText());
        registryNoticeBtn.click();
        Thread.sleep(1500L);
    }


    public void signIn() {
        String packageReviewPageUrl = MessageFormat.format("{0}/{1}", packageReviewUrl, packageId);
        logger.info("Formatted package review page url:{}", packageReviewPageUrl);

        this.driver.get(packageReviewPageUrl);
        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();
    }
}
