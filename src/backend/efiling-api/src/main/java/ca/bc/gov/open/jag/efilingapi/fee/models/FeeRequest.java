package ca.bc.gov.open.jagefilingapi.fee.models;

/**
 * Represent a request to get a fee
 */
public class FeeRequest {

    private String documentType;
    private String documentSubType;

    public FeeRequest(String documentType, String documentSubType) {
        this.documentType = documentType;
        this.documentSubType = documentSubType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentSubType() {
        return documentSubType;
    }

}
