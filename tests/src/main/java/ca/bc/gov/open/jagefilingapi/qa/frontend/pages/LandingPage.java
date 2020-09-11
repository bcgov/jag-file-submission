package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;

public class LandingPage {

    private final WebDriver driver;

    Logger log = LogManager.getLogger(LandingPage.class);
    ReadConfig readConfig;

    //Page Objects:
    @FindBy(id = "textInputId")
    WebElement guidInputForm;

    @FindBy(xpath = "//button[@data-test-id='generate-url-btn']")
    WebElement generateUrlButton;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/span[1]")
    WebElement getErrorText;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div[1]/div/input")
    WebElement chooseFile;

    @FindBy(xpath = "//*[starts-with(@id,'filepond--item')]")
    WebElement selectedFile;

    @FindBy(xpath = "//*[@id='1']")
    WebElement textInput;

    //Initializing the driver:
    public LandingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public String verifyLandingPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));
        log.info("Waiting for the page to load...");
        return driver.getTitle();
    }

    public LandingPage getUrl() throws IOException {
        readConfig = new ReadConfig();
        String url = readConfig.getBaseUrl();
        driver.get(url);
        return new LandingPage(driver);
    }

    public void enterAccountGuid(String guid) {
         guidInputForm.sendKeys(guid);
    }

    public void clickEfilePackageButton() {
        generateUrlButton.click();
    }

    public String getErrorMessageText() {
        return getErrorText.getText();
    }

    public void chooseFileToUpload(String file) {
        chooseFile.sendKeys(file);
    }

    public void enterJsonData() {
        String json  = "{\n" +
                "    \"court\": {\n" +
                "        \"location\": \"1211\",\n" +
                "        \"level\": \"P\",\n" +
                "        \"courtClass\": \"F\",\n" +
                "        \"division\": \"I\",\n" +
                "        \"fileNumber\": \"1234\",\n" +
                "        \"participatingClass\": \"string\"\n" +
                "    },\n" +
                "    \"documents\": [{\n" +
                "        \"name\": \"test-document.pdf\",\n" +
                "        \"type\": \"AFF\"\n" +
                "    }],\n" +
                "    \"parties\": [{\n" +
                "        \"partyType\": \"IND\",\n" +
                "        \"roleType\": \"CLA\",\n" +
                "        \"firstName\": \"efile\",\n" +
                "        \"middleName\": \"test\",\n" +
                "        \"lastName\": \"qa\",\n" +
                "    }]\n" +
                "}";


        textInput.sendKeys(json);

    }
}
