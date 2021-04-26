package ca.bc.gov.open.jag.aireviewermockapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DocumentReady {
    @JsonProperty
    private BigDecimal documentId;
    @JsonProperty
    private String documentType;
    @JsonProperty
    private String returnUri;

    public DocumentReady() {}

    public DocumentReady(@JsonProperty("documentId") BigDecimal documentId,
                         @JsonProperty("documentType") String documentType,
                         @JsonProperty("returnUri") String returnUri) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.returnUri = returnUri;
    }

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
