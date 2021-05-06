package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiReviewerAdminClientPage extends BasePage {

    private final Logger logger = LoggerFactory.getLogger(AiReviewerAdminClientPage.class);

    @FindBy(xpath = "//*[@data-testid='add-btn']")
    private WebElement addConfigBtn;

    @FindBy(xpath = "//*[@id='root']/div/div/button[2]")
    private WebElement submitBtn;

    @FindBy(xpath = "//*[@id='root']/div/div/button[1]")
    private WebElement cancelBtn;

    @FindBy(id = "new-config-textfield")
    private WebElement jsonInputTextArea;

    @FindBy(xpath = "//*[@data-testid='update-btn']")
    private WebElement updateConfigBtn;

    @FindBy(xpath = "//*[contains(@data-testid, 'delete')]")
    private WebElement deleteConfigBtn;


}
