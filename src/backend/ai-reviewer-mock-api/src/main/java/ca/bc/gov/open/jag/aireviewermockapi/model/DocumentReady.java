package ca.bc.gov.open.jag.aireviewermockapi.model;

import java.math.BigDecimal;

public class DocumentReady {
    private BigDecimal documentId;
    private String documentType;
    private String returnUri;

    public BigDecimal getDocumentId() {
        return documentId;
    }

    public void setDocumentId(BigDecimal documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getReturnUri() {
        return returnUri;
    }

    public void setReturnUri(String returnUri) {
        this.returnUri = returnUri;
    }
}
