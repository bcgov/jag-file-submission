package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AiReviewerAdminClientPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class ConfigureDocumentTypesInAiReviewerAdminSD {

    @Value("${AI_REVIEWER_ADMIN_CLIENT_URL:http://localhost:3002}")
    private String aiReviewerAdminClientUrl;

    private final AiReviewerAdminClientPage aiReviewerAdminClientPage;

    private static final Logger logger = LoggerFactory.getLogger(ConfigureDocumentTypesInAiReviewerAdminSD.class);

    public ConfigureDocumentTypesInAiReviewerAdminSD(AiReviewerAdminClientPage aiReviewerAdminClientPage) {
        this.aiReviewerAdminClientPage = aiReviewerAdminClientPage;
    }

    @Given("user adds and submits new document type configuration")
    public void addANewDocumentTypeConfiguration() throws IOException {
        logger.info("Navigating to Ai reviewer admin client");

        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.addNewDocTypeConfiguration();

        logger.info("Added new document config json and submitted");
    }

    @Then("new document type configuration is created")
    public void validateDocumentTypeConfigurationIsCreated() {
        logger.info("Validating the document type and document description");

        Assert.assertEquals("RCC", this.aiReviewerAdminClientPage.getDocumentType());
        Assert.assertEquals("Response to Civil Claim", this.aiReviewerAdminClientPage.getDocumentDescription());
    }

    @Given("user updates and submits existing document type configuration")
    public void updateAnExistingDocumentTypeConfiguration() {
        logger.info("Navigating to Ai reviewer admin page");

        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.updateDocTypeConfiguration();

        logger.info("Updated existing document config json and submitted");
    }

    @Then("document type configuration is updated")
    public void validateDocumentTypeConfigurationIsUpdated() {
        logger.info("Validating the updated document description");

        Assert.assertEquals("RCC", this.aiReviewerAdminClientPage.getDocumentType());
        Assert.assertEquals("Updated Response to Civil Claim", this.aiReviewerAdminClientPage.getDocumentDescription());
    }

    @Given("user deletes an existing document type configuration")
    public void deleteADocumentTypeConfiguration() {
        logger.info("Navigating to Ai reviewer admin url");

        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.deleteDocTypeConfiguration();

        logger.info("Deleted existing document config json and submitted");

    }

    @Then("document type configuration is removed from the admin client")
    public void validateDocumentTypeConfigurationIsRemoved() {
        logger.info("Validating the updated document description");

        Assert.assertTrue(this.aiReviewerAdminClientPage.verifyTheDocumentListIsEmpty());
    }
}
