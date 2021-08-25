package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Document {

    private BigDecimal documentId;
    private String name;
    private String type;
    private String subType;
    private Boolean isAmendment;
    private Boolean isSupremeCourtScheduling;
    private Object data;
    private String description;
    private BigDecimal statutoryFeeAmount;
    private String mimeType;
    private String serverFileName;

    @JsonCreator
    public Document(@JsonProperty("documentId") BigDecimal documentId,
                    @JsonProperty("name") String name,
                    @JsonProperty("type") String type,
                    @JsonProperty("subType") String subType,
                    @JsonProperty("isAmendment") Boolean isAmendment,
                    @JsonProperty("isSupremeCourtScheduling") Boolean isSupremeCourtScheduling,
                    @JsonProperty("data") Object data,
                    @JsonProperty("description") String description,
                    @JsonProperty("statutoryFeeAmount") BigDecimal statutoryFeeAmount,
                    @JsonProperty("mimeType") String mimeType,
                    @JsonProperty("serverFileName") String serverFileName) {
        this.documentId = documentId;
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.isAmendment = isAmendment;
        this.isSupremeCourtScheduling = isSupremeCourtScheduling;
        this.data = data;
        this.description = description;
        this.statutoryFeeAmount = statutoryFeeAmount;
        this.mimeType = mimeType;
        this.serverFileName = serverFileName;
    }

    public Document(Builder builder) {
        this.documentId = builder.documentId;
        this.name = builder.name;
        this.type = builder.type;
        this.subType = builder.subType;
        this.isAmendment = builder.isAmendment;
        this.isSupremeCourtScheduling = builder.isSupremeCourtScheduling;
        this.data = builder.data;
        this.description = builder.description;
        this.statutoryFeeAmount = builder.statutoryFeeAmount;
        this.mimeType = builder.mimeType;
        this.serverFileName = builder.serverFileName;
    }

    public BigDecimal getDocumentId() { return documentId; }

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

    public String getServerFileName() {
        return serverFileName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BigDecimal documentId;
        private String name;
        private String type;
        private String subType;
        private Boolean isAmendment;
        private Boolean isSupremeCourtScheduling;
        private Object data;
        private String description;
        private BigDecimal statutoryFeeAmount;
        private String mimeType;
        private String serverFileName;

        public Builder documentId(BigDecimal documentId) {
            this.documentId = documentId;
            return this;
        }

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

        public Builder serverFileName(String serverFileName) {
            this.serverFileName = serverFileName;
            return this;
        }

        public Document create() {
            return new Document(this);
        }
    }

}
