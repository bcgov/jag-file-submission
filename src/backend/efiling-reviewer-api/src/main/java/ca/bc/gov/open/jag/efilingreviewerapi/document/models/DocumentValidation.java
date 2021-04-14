package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DocumentValidation {

    List<DocumentValidationResult> validationResults;

    public DocumentValidation(@JsonProperty("validationResults") List<DocumentValidationResult> validationResults) {
        this.validationResults = validationResults;
    }

    public List<DocumentValidationResult> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(List<DocumentValidationResult> validationResults) {
        this.validationResults = validationResults;
    }
}
