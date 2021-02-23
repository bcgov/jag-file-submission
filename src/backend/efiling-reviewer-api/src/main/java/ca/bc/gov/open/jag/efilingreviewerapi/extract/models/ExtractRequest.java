package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtractRequest {

    private Extract extract;
    private Document document;

    public ExtractRequest(
            @JsonProperty("extract") Extract extract,
            @JsonProperty("document") Document document) {
        this.extract = extract;
        this.document = document;
    }

    public ExtractRequest(Builder builder) {
        this.document = builder.document;
        this.extract = builder.extract;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Extract extract;

        public Builder extract(Extract extract) {
            this.extract = extract;
            return this;
        }

        private Document document;

        public Builder document(Document document) {
            this.document = document;
            return this;
        }

        public ExtractRequest create() {
            return new ExtractRequest(this);
        }

    }

    public Extract getExtract() {
        return extract;
    }

    public Document getDocument() {
        return document;
    }
}
