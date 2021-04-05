package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SubmissionHistoryPage extends BasePage {

    @Value("${SUBMISSION_HISTORY_URL:http://localhost:3000/efilinghub/submissionhistory}")
    private String submissionHistoryUrl;

    @Value("${USERNAME_KEYCLOAK:bobross}")
    private String username;

    @Value("${PASSWORD_KEYCLOAK:changeme}")
    private String password;

    private Logger logger = LoggerFactory.getLogger(SubmissionHistoryPage.class);

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    @FindBy(className = "submission-list-container")
    private WebElement submissionList;

    public boolean submissionListIsDisplayed() {
        return submissionList.isDisplayed();
    }

    public void signIn() {

        this.driver.get(submissionHistoryUrl);
        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();
    }
}
