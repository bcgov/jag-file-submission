package ca.bc.gov.open.jag.efilingreviewerapi.webhook.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DocumentReady {

    private BigDecimal documentId;
    private String documentType;
    private String returnUri;

    public DocumentReady() {
    }

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

    public String getDocumentType() {
        return documentType;
    }

    public String getReturnUri() {
        return returnUri;
    }

    public void setDocumentId(BigDecimal documentId) {
        this.documentId = documentId;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setReturnUri(String returnUri) {
        this.returnUri = returnUri;
    }

    public DocumentReady(DocumentReady.Builder builder) {
        this();
        this.documentId = builder.documentId;
        this.documentType = builder.documentType;
        this.returnUri = builder.returnUri;
    }

    public static DocumentReady.Builder builder() {
        return new DocumentReady.Builder();
    }

    public static class Builder {

        private BigDecimal documentId;
        private String documentType;
        private String returnUri;

        public DocumentReady.Builder documentId(BigDecimal documentId) {
            this.documentId = documentId;
            return this;
        }

        public DocumentReady.Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public DocumentReady.Builder returnUri(String returnUri) {
            this.returnUri = returnUri;
            return this;
        }

        public DocumentReady create() {
            return new DocumentReady(this);
        }

    }
}
