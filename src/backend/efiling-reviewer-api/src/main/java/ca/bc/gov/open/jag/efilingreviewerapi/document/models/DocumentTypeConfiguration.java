package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class DocumentTypeConfiguration {

    @Id
    private UUID id;

    private String documentType;

    private DocumentConfig documentConfig;

    public DocumentTypeConfiguration() {
        id = UUID.randomUUID();
    }

    public DocumentTypeConfiguration(
            @JsonProperty("id") UUID id,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("formData") DocumentConfig formData) {
        this.id = id;
        this.documentType = documentType;
        this.documentConfig = formData;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public DocumentConfig getDocumentConfig() {
        return documentConfig;
    }

    public DocumentTypeConfiguration(Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.documentConfig = builder.documentConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String documentType;
        private DocumentConfig documentConfig;

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder documentConfig(DocumentConfig documentConfig) {
            this.documentConfig = documentConfig;
            return this;
        }

        public DocumentTypeConfiguration create() {
            return new DocumentTypeConfiguration(this);
        }

    }

}
