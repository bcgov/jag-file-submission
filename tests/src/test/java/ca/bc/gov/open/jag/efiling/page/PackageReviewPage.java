package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PackageReviewPage extends Base {

    @FindBy(className = "bcgov-row")
    private List<WebElement> packageDetailsRow;

    @FindBy(className = "bcgov-fill-width")
    private List<WebElement> packageKeyDetails;

    @FindBy(xpath = "//*[contains(@class,'bcgov-table-value')]/b")
    private List<WebElement> packageValueDetails;

    @FindBy(id = "uncontrolled-tab-tab-documents")
    private WebElement documentsTab;

    @FindBy(id = "uncontrolled-tab-tab-comments")
    private WebElement filingCommentsTab;

    @FindBy(id = "uncontrolled-tab-tabpane-documents")
    private WebElement documentPane;

    @FindBy(id = "filingComments")
    private WebElement filingCommentsTextBox;

    Logger logger = LoggerFactory.getLogger(PackageReviewPage.class);

    public void goTo(String url) {
        this.driver.get(url);
    }

    public List<String> getPackageDetails() {

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("bcgov-row")));

        logger.info("Getting package details values");

        List<String> packageDetails = new ArrayList<>();
        packageValueDetails.forEach(webElement -> {
            packageDetails.add(webElement.getText());
            System.out.println(packageDetails);
        });
        return packageDetails;
    }

    public void clickDocumentsTab() {
        documentsTab.click();
    }

    public void clickFilingCommentsTab() {
        filingCommentsTab.click();
    }

    public boolean verifyDocumentsPaneIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(documentPane));
        return documentPane.isDisplayed();
    }

    public boolean verifyFilingCommentsIsDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(filingCommentsTextBox));
        return filingCommentsTextBox.isDisplayed();
    }
}
