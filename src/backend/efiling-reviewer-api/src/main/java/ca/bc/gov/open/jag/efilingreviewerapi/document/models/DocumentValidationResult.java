package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentValidationResult {
    private ValidationTypes type;
    private String expected;
    private String actual;

    public DocumentValidationResult(@JsonProperty("type") ValidationTypes type,
                                    @JsonProperty("expected") String expected,
                                    @JsonProperty("actual") String actual) {
        this.type = type;
        this.expected = expected;
        this.actual = actual;

    }

    public ValidationTypes getType() {
        return type;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }

    public DocumentValidationResult(DocumentValidationResult.Builder builder) {
        this.type = builder.type;
        this.expected = builder.expected;
        this.actual = builder.actual;
    }

    public static DocumentValidationResult.Builder builder() {
        return new DocumentValidationResult.Builder();
    }

    public static class Builder {
        private ValidationTypes type;
        private String expected;
        private String actual;

        public DocumentValidationResult.Builder type(ValidationTypes type) {
            this.type = type;
            return this;
        }

        public DocumentValidationResult.Builder expected(String expected) {
            this.expected = expected;
            return this;
        }

        public DocumentValidationResult.Builder actual(String actual) {
            this.actual = actual;
            return this;
        }

        public DocumentValidationResult create() {
            return new DocumentValidationResult(this);
        }

    }
}
