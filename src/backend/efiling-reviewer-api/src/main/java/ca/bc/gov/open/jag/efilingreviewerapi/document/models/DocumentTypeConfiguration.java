package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class DocumentTypeConfiguration {

    @Id
    private UUID id;

    private String documentType;

    private String jsonSchema;

    public DocumentTypeConfiguration() {
        id = UUID.randomUUID();
    }

    public DocumentTypeConfiguration(
            @JsonProperty("id") UUID id,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("jsonSchema") String jsonSchema) {
        this.id = id;
        this.documentType = documentType;
        this.jsonSchema = jsonSchema;
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

    public String getJsonSchema() {
        return jsonSchema;
    }

    public DocumentTypeConfiguration(Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.jsonSchema = builder.jsonSchema;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String documentType;
        private String jsonSchema;

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder jsonSchema(String jsonSchema) {
            this.jsonSchema = jsonSchema;
            return this;
        }

        public DocumentTypeConfiguration create() {
            return new DocumentTypeConfiguration(this);
        }

    }

}
