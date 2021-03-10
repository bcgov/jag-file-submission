package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;

public class BceidAuthenticationPageImpl extends BasePage implements AuthenticationPage {

    @Value("${USERNAME_BCEID}")
    private String bceidUsername;

    @Value("${PASSWORD_BCEID}")
    private String bceidPassword;

    //Page Objects:
    @FindBy(id = "zocial-bceid")
    private WebElement bceid;

    @FindBy(id = "user")
    private WebElement bceidUsernameField;

    @FindBy(id = "password")
    private WebElement bceidPasswordField;

    @FindBy(xpath = "//input[@name='btnSubmit']")
    private WebElement submitButton;

    public void loginWithBceid() {
        wait.until(ExpectedConditions.titleContains("Log in to Family Law Act Application"));
        bceid.click();
        wait.until(ExpectedConditions.visibilityOf(bceidUsernameField));
        bceidUsernameField.sendKeys(bceidUsername);
        bceidPasswordField.sendKeys(bceidPassword);
        submitButton.click();
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));
    }

    @Override
    public String getName() {
        return "bceid";
    }

    @Override
    public void signIn() {
        loginWithBceid();
    }
}
