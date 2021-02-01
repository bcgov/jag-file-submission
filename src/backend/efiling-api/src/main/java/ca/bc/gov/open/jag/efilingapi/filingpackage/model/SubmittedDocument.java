package ca.bc.gov.open.jag.efilingapi.filingpackage.model;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

public class SubmittedDocument {
    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public SubmittedDocument(SubmittedDocument.Builder builder) {
        this.name = builder.name;
        this.data = builder.data;
    }

    public static SubmittedDocument.Builder builder() {
        return new SubmittedDocument.Builder();
    }

    public static class Builder {

        private String name;
        private byte[] data;


        public SubmittedDocument.Builder name(String name) {
            this.name = name;
            return this;
        }

        public SubmittedDocument.Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public SubmittedDocument create() {
            return new SubmittedDocument(this);
        }
    }
}
