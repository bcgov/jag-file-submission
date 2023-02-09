package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Document {

    private String name;
    private String type;
    private String subType;
    private Boolean isAmendment;
    private Boolean isSupremeCourtScheduling;
    private Object data;
    private String description;
    private BigDecimal statutoryFeeAmount;
    private String mimeType;
    private String documentId;
    private String serverFileName;
    private ActionDocument actionDocument;

    @JsonCreator
    public Document(@JsonProperty("name") String name,
                    @JsonProperty("type") String type,
                    @JsonProperty("subType") String subType,
                    @JsonProperty("isAmendment") Boolean isAmendment,
                    @JsonProperty("isSupremeCourtScheduling") Boolean isSupremeCourtScheduling,
                    @JsonProperty("data") Object data,
                    @JsonProperty("description") String description,
                    @JsonProperty("statutoryFeeAmount") BigDecimal statutoryFeeAmount,
                    @JsonProperty("mimeType") String mimeType,
                    @JsonProperty("documentId") String documentId,
                    @JsonProperty("serverFileName") String serverFileName,
                    @JsonProperty("actionDocument") ActionDocument actionDocument) {
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.isAmendment = isAmendment;
        this.isSupremeCourtScheduling = isSupremeCourtScheduling;
        this.data = data;
        this.description = description;
        this.statutoryFeeAmount = statutoryFeeAmount;
        this.mimeType = mimeType;
        this.documentId = documentId;
        this.serverFileName = serverFileName;
        this.actionDocument = actionDocument;
    }

    public Document(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.subType = builder.subType;
        this.isAmendment = builder.isAmendment;
        this.isSupremeCourtScheduling = builder.isSupremeCourtScheduling;
        this.data = builder.data;
        this.description = builder.description;
        this.statutoryFeeAmount = builder.statutoryFeeAmount;
        this.mimeType = builder.mimeType;
        this.documentId = builder.documentId;
        this.serverFileName = builder.serverFileName;
        this.actionDocument = builder.actionDocument;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public Boolean getIsAmendment() {
        return isAmendment;
    }

    public Boolean getIsSupremeCourtScheduling() {
        return isSupremeCourtScheduling;
    }

    public Object getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getStatutoryFeeAmount() {
        return statutoryFeeAmount;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public ActionDocument getActionDocument() { return actionDocument; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String type;
        private String subType;
        private Boolean isAmendment;
        private Boolean isSupremeCourtScheduling;
        private Object data;
        private String description;
        private BigDecimal statutoryFeeAmount;
        private String mimeType;
        private String documentId;
        private String serverFileName;
        private ActionDocument actionDocument;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public Builder isAmendment(Boolean isAmendment) {
            this.isAmendment = isAmendment;
            return this;
        }

        public Builder isSupremeCourtScheduling(Boolean isSupremeCourtScheduling) {
            this.isSupremeCourtScheduling = isSupremeCourtScheduling;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder statutoryFeeAmount(BigDecimal statutoryFeeAmount) {
            this.statutoryFeeAmount = statutoryFeeAmount;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder documentId(String documentId) {
            this.documentId = documentId;
            return this;
        }

        public Builder serverFileName(String serverFileName) {
            this.serverFileName = serverFileName;
            return this;
        }

        public Builder actionDocument(ActionDocument actionDocument) {
            this.actionDocument = actionDocument;
            return this;
        }

        public Document create() {
            return new Document(this);
        }
    }

}
