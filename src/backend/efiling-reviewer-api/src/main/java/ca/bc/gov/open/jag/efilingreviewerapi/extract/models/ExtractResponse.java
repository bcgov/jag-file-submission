package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExtractResponse {

    private Extract extract;
    private Document document;
    private DocumentValidation documentValidation;
    private ObjectNode formData;

    public ExtractResponse(@JsonProperty("extract") Extract extract,
                           @JsonProperty("document") Document document,
                           @JsonProperty("documentValidation") DocumentValidation documentValidation,
                           @JsonProperty("formData")ObjectNode formData) {
        this.extract = extract;
        this.document = document;
        this.documentValidation = documentValidation;
        this.formData = formData;
    }

    public Extract getExtract() {
        return extract;
    }

    public Document getDocument() {
        return document;
    }

    public ObjectNode getFormData() {
        return formData;
    }

    public DocumentValidation getDocumentValidation() { return documentValidation; }

    public ExtractResponse(Builder builder) {
        this.extract = builder.extract;
        this.document = builder.document;
        this.documentValidation = builder.documentValidation;
        this.formData = builder.formData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Extract extract;
        private Document document;
        private DocumentValidation documentValidation;
        private ObjectNode formData;

        public Builder extract(Extract extract) {
            this.extract = extract;
            return this;
        }

        public Builder document(Document document) {
            this.document = document;
            return this;
        }

        public Builder documentValidation(DocumentValidation documentValidation) {
            this.documentValidation = documentValidation;
            return this;
        }

        public Builder formData(ObjectNode formData) {
            this.formData = formData;
            return this;
        }

        public ExtractResponse create() {
            return new ExtractResponse(this);
        }

    }

}
