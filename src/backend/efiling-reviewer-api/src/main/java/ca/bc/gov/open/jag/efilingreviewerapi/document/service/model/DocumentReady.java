package ca.bc.gov.open.jag.efilingreviewerapi.document.service.model;


import java.math.BigDecimal;

public class DocumentReady {
    private BigDecimal documentId;
    private String documentType;
    private String returnUri;

    public DocumentReady() {
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
