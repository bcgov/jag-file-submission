package ca.bc.gov.open.jag.efilingapi.fee.models;

/**
 * Represent a request to get a fee
 */
public class FeeRequest {

    private String documentType;

    public FeeRequest(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }

}
