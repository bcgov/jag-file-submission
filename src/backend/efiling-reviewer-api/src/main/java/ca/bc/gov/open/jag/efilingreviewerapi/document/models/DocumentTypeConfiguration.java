package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.FormData;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class DocumentTypeConfiguration {

    @Id
    private UUID id;

    private String documentType;

    private FormData formData;

    public DocumentTypeConfiguration() {
        id = UUID.randomUUID();
    }

    public DocumentTypeConfiguration(
            @JsonProperty("id") UUID id,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("formData") FormData formData) {
        this.id = id;
        this.documentType = documentType;
        this.formData = formData;
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

    public FormData getFormData() {
        return formData;
    }

    public DocumentTypeConfiguration(Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.formData = builder.formData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String documentType;
        private FormData formData;

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder formData(FormData formData) {
            this.formData = formData;
            return this;
        }

        public DocumentTypeConfiguration create() {
            return new DocumentTypeConfiguration(this);
        }

    }

}
