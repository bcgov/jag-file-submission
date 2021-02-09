package ca.bc.gov.open.jag.efiling.page;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class AuthenticationPage extends Base {

    Logger log = LogManager.getLogger(AuthenticationPage.class);

    @Value("${USERNAME_BCEID:bobross}")
    private String username;

    @Value("${PASSWORD_BCEID:changeme}")
    private String password;

    //Page Objects:
    @FindBy(id = "username")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "kc-login")
    WebElement signIn;

    private final GenerateUrlService generateUrlService;

    public AuthenticationPage(GenerateUrlService generateUrlService) {
        this.generateUrlService = generateUrlService;
    }

    //Actions
    public void goTo() throws IOException {
        String actualGeneratedRedirectUrl = generateUrlService.getGeneratedUrl();

        if(actualGeneratedRedirectUrl == null) throw new EfilingTestException("Redirect url is not generated.");
        this.driver.get(actualGeneratedRedirectUrl);

    }

    public void signInWithBceid() {
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));
        log.info("Waiting for the page to load...");
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();
    }
}
