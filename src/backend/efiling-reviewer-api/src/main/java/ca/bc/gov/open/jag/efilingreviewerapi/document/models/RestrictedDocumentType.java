package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class RestrictedDocumentType {
    @Id
    private UUID id;

    private String documentType;

    private String documentTypeDescription;

    public RestrictedDocumentType() {
        id = UUID.randomUUID();
    }

    public RestrictedDocumentType(
            @JsonProperty("id") UUID id,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("documentTypeDescription") String documentTypeDescription) {
        this.id = id;
        this.documentType = documentType;
        this.documentTypeDescription = documentTypeDescription;
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

    public String getDocumentTypeDescription() { return documentTypeDescription;  }

    public RestrictedDocumentType(RestrictedDocumentType.Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.documentTypeDescription = builder.documentTypeDescription;
    }

    public static RestrictedDocumentType.Builder builder() {
        return new RestrictedDocumentType.Builder();
    }

    public static class Builder {

        private String documentType;
        private String documentTypeDescription;

        public RestrictedDocumentType.Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public RestrictedDocumentType.Builder documentTypeDescription(String documentTypeDescription) {
            this.documentTypeDescription = documentTypeDescription;
            return this;
        }

        public RestrictedDocumentType create() {
            return new RestrictedDocumentType(this);
        }

    }
}
