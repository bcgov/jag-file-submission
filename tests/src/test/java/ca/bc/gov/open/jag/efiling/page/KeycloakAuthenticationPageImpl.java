package ca.bc.gov.open.jag.efiling.page;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class KeycloakAuthenticationPageImpl extends BasePage implements AuthenticationPage {

    @Value("${USERNAME_KEYCLOAK:bobross}")
    private String username;

    @Value("${PASSWORD_KEYCLOAK:changeme}")
    private String password;

    private final GenerateUrlService generateUrlService;

    private Logger logger = LoggerFactory.getLogger(ca.bc.gov.open.jag.efiling.page.KeycloakAuthenticationPageImpl.class);

    //Page Objects:
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    public KeycloakAuthenticationPageImpl(GenerateUrlService generateUrlService) {
        this.generateUrlService = generateUrlService;
    }

    @Override
    public void goTo(String url) {
        this.driver.get(url);
    }

    @Override
    public String getName() {
        return "keycloak";
    }

    @Override
    public void signIn() {
        String actualGeneratedRedirectUrl = this.generateUrlService.getGeneratedUrl();
        if (actualGeneratedRedirectUrl == null) throw new EfilingTestException("Redirect url is not generated.");
        goTo(actualGeneratedRedirectUrl);
        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();
    }
}
