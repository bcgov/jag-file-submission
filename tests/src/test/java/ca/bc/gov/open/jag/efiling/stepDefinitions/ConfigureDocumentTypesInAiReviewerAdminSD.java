package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AiReviewerAdminClientPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    @Given("user adds a new document type configuration")
    public void addANewDocumentTypeConfiguration() throws IOException {

        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.addNewDocTypeConfiguration();
    }

    @When("document type config is submitted")
    public void submitDocumentTypeConfig() {

    }

    @Then("new document type configuration is created")
    public void validateDocumentTypeConfigurationIsCreated() {

    }

    @Given("user updates an existing document type configuration")
    public void updateAnExistingDocumentTypeConfiguration() throws IOException {
        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.updateDocTypeConfiguration();

    }

    @When("updated document configuration details are submitted")
    public void submitUpdatedDocumentConfigurationDetails() {

    }

    @Then("document type configuration is updated")
    public void validateDocumentTypeConfigurationIsUpdated() {

    }

    @Given("user deletes an existing document type configuration")
    public void deleteADocumentTypeConfiguration() throws IOException {

        this.aiReviewerAdminClientPage.goTo(aiReviewerAdminClientUrl);
        this.aiReviewerAdminClientPage.deleteDocTypeConfiguration();

    }

    @Then("document type configuration is removed from the admin client")
    public void validateDocumentTypeConfigurationIsRemoved() {

    }
}
