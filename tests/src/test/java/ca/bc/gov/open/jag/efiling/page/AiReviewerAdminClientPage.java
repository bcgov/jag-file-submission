package ca.bc.gov.open.jag.efiling.page;

import ca.bc.gov.open.jag.efiling.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

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

    public void addNewDocTypeConfiguration() throws IOException {

        addConfigBtn.click();

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", Keys.DOCUMENT_TYPE_CONFIG_PAYLOAD)).getFile();

        String configJson = new String(Files.readAllBytes(Paths.get(String.valueOf(resource))));

        jsonInputTextArea.sendKeys(configJson);
        submitBtn.click();

    }

    public void updateDocTypeConfiguration() throws IOException {

        updateConfigBtn.click();
        String actualJson = jsonInputTextArea.getText().replace("Response to Civil Claim", "Updated Response to Civil Claim");

        jsonInputTextArea.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        jsonInputTextArea.sendKeys(org.openqa.selenium.Keys.DELETE);

        jsonInputTextArea.sendKeys(actualJson);
        submitBtn.click();

    }

    public void deleteDocTypeConfiguration() throws IOException {

        deleteConfigBtn.click();

    }

}
