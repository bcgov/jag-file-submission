package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Document {

    private String type;
    private String fileName;
    private BigDecimal size;
    private String contentType;

    public Document(
            @JsonProperty("type") String type,
            @JsonProperty("fileName") String fileName,
            @JsonProperty("size") BigDecimal size,
            @JsonProperty("contentType") String contentType) {
        this.type = type;
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public BigDecimal getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public Document(Builder builder) {
        this.contentType = builder.contentType;
        this.fileName = builder.fileName;
        this.size = builder.size;
        this.type = builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String type;
        private String fileName;
        private BigDecimal size;
        private String contentType;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Document create() {
            return new Document(this);
        }

        public Builder size(BigDecimal size) {
            this.size = size;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

    }
}
