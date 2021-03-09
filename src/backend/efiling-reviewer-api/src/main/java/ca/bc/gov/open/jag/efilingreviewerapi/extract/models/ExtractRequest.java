package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtractRequest {

    private Extract extract;
    private Document document;
    private Long receivedTimeMillis;
    private Long processedTimeMillis;

    public ExtractRequest(
            @JsonProperty("extract") Extract extract,
            @JsonProperty("document") Document document,
            @JsonProperty("receivedTimeMillis") Long receivedTimeMillis,
            @JsonProperty("processedTimeMillis") Long processedTimeMillis) {
        this.extract = extract;
        this.document = document;
        this.receivedTimeMillis = receivedTimeMillis;
        this.processedTimeMillis = processedTimeMillis;
    }

    public ExtractRequest(Builder builder) {
        this.document = builder.document;
        this.extract = builder.extract;
        this.receivedTimeMillis = builder.receivedTimeMillis;
    }

    public void updateProcessedTimeMillis() {
        this.processedTimeMillis = System.currentTimeMillis();
    }

    public Long getReceivedTimeMillis() {
        return receivedTimeMillis;
    }

    public long getProcessedTimeMillis() {

        if (processedTimeMillis == null || receivedTimeMillis == null) {
            return -1L;
        }

        return processedTimeMillis -  receivedTimeMillis;

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Extract extract;
        private Document document;
        private long receivedTimeMillis;

        public Builder extract(Extract extract) {
            this.extract = extract;
            return this;
        }

        public Builder document(Document document) {
            this.document = document;
            return this;
        }

        public Builder receivedTimeMillis(long receivedTimeMillis) {
            this.receivedTimeMillis = receivedTimeMillis;
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
