package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import java.util.List;

public class DocumentValidation {

    List<DocumentValidationResult> validationResults;

    public DocumentValidation(List<DocumentValidationResult> validationResults) {
        this.validationResults = validationResults;
    }

    public List<DocumentValidationResult> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(List<DocumentValidationResult> validationResults) {
        this.validationResults = validationResults;
    }
}
