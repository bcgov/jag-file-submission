package ca.bc.gov.open.jag.efiling.page;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;

public class BceidAuthenticationPage extends BasePage implements AuthenticationPage {

    private Logger logger = LogManager.getLogger(BceidAuthenticationPage.class);
    private final GenerateUrlService generateUrlService;

    @Value("${USERNAME_BCEID:bobross}")
    private String username;

    @Value("${PASSWORD_BCEID:changeme}")
    private String password;

    //Page Objects:
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    public BceidAuthenticationPage(GenerateUrlService generateUrlService) {
        this.generateUrlService = generateUrlService;
    }

    @Override
    public void goTo(String url) {
        this.driver.get(url);
    }

    @Override
    public String getName() {
        return "bceid";
    }

    @Override
    public void signIn() {
        String actualGeneratedRedirectUrl = this.generateUrlService.getGeneratedUrl();
        if(actualGeneratedRedirectUrl == null) throw new EfilingTestException("Redirect url is not generated.");
        goTo(actualGeneratedRedirectUrl);
        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();

    }

}
