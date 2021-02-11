package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PackageReviewPage extends BasePage {

    private Logger logger = LoggerFactory.getLogger(PackageReviewPage.class);

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

    public List<String> getPackageDetails() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-test-id='detailsTab']")));

        logger.info("Getting package details values");

        List<String> packageDetails = new ArrayList<>();
        packageValueDetails.forEach(webElement -> {
            if (!webElement.isDisplayed()) {
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'bcgov-table-value')]/b")));
            }
            logger.info("Is it visible: {}", webElement.isDisplayed());
            logger.info("Is it enabled: {}", webElement.isEnabled());
            packageDetails.add(webElement.getText());
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
