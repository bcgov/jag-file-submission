package ca.bc.gov.open.jag.efilingapi.document;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Represents a document for storage usage.
 */
public class Document {

    private UUID submissionId;

    private String fileName;

    private byte[] content;

    protected Document(Builder builder) {
        this.submissionId = builder.submissionId;
        this.fileName = builder.fileName;
        this.content = builder.content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public byte[] getContent() {
        return content;
    }

    public String getCompositeId() {
        return MessageFormat.format("{0}_{1}", submissionId, fileName);
    }

    public static class Builder {

        private UUID submissionId;
        private String fileName;
        private byte[] content;

        public Builder submissionId (UUID submissionId) {
            this.submissionId = submissionId;
            return this;
        }

        public Builder fileName (String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder content (byte[] content) {
            this.content = content;
            return this;
        }

        public Document create() {
            return new Document(this);
        }

    }

}