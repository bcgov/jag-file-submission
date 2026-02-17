package ca.bc.gov.open.jag.efilingapi.filingpackage.model;

import org.springframework.core.io.Resource;

public class SubmittedDocument {
    private String name;
    private Resource data;

    public String getName() {
        return name;
    }

    public Resource getData() {
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
        private Resource data;


        public SubmittedDocument.Builder name(String name) {
            this.name = name;
            return this;
        }

        public SubmittedDocument.Builder data(Resource data) {
            this.data = data;
            return this;
        }

        public SubmittedDocument create() {
            return new SubmittedDocument(this);
        }
    }
}
