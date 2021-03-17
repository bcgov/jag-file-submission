package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExtractResponse {

    private Extract extract;
    private Document document;
    private ObjectNode formData;

    public ExtractResponse(Extract extract, Document document, ObjectNode formData) {
        this.extract = extract;
        this.document = document;
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

    public ExtractResponse(Builder builder) {
        this.extract = builder.extract;
        this.document = builder.document;
        this.formData = builder.formData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Extract extract;
        private Document document;
        private ObjectNode formData;

        public Builder extract(Extract extract) {
            this.extract = extract;
            return this;
        }

        public Builder document(Document document) {
            this.document = document;
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
