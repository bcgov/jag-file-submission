package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import java.util.List;

public class DocumentValidationResult {

    List<DocumentValidations> validationResults;

    public DocumentValidationResult(List<DocumentValidations> validationResults) {
        this.validationResults = validationResults;
    }

    public List<DocumentValidations> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(List<DocumentValidations> validationResults) {
        this.validationResults = validationResults;
    }
}
