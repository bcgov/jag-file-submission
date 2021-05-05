package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class AiReviewerAdminClientPage extends BasePage {

    @Value("${AI_REVIEWER_ADMIN_CLIENT_URL:http://localhost:3002}")
    private String aiReviewerAdminClientUrl;

    private final Logger logger = LoggerFactory.getLogger(AiReviewerAdminClientPage.class);

    @FindBy(xpath = "//*[@data-testid='add-btn']")
    private WebElement addConfigButton;


}
