package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ActionDocument {

    private BigDecimal documentId;
    private String status;
    private String type;

    @JsonCreator
    public ActionDocument(
            @JsonProperty("documentId") BigDecimal documentId,
            @JsonProperty("status") String status,
            @JsonProperty("type") String type) {
        this.documentId = documentId;
        this.status = status;
        this.type = type;
    }

    public ActionDocument(ActionDocument.Builder builder) {
        this.documentId = builder.documentId;
        this.status = builder.status;
        this.type = builder.type;
    }

    public BigDecimal getDocumentId() { return documentId; }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public static ActionDocument.Builder builder() {
        return new ActionDocument.Builder();
    }

    public static class Builder {

        private BigDecimal documentId;
        private String status;
        private String type;

        public ActionDocument.Builder documentId(BigDecimal documentId) {
            this.documentId = documentId;
            return this;
        }

        public ActionDocument.Builder status(String status) {
            this.status = status;
            return this;
        }

        public ActionDocument.Builder type(String type) {
            this.type = type;
            return this;
        }

        public ActionDocument create() {
            return new ActionDocument(this);
        }

    }

}
