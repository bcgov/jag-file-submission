package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

public class DocumentValidations {
    private ValidationTypes type;
    private String expected;
    private String actual;

    public ValidationTypes getType() {
        return type;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }

    public DocumentValidations(DocumentValidations.Builder builder) {
        this.type = builder.type;
        this.expected = builder.expected;
        this.actual = builder.actual;
    }

    public static DocumentValidations.Builder builder() {
        return new DocumentValidations.Builder();
    }

    public static class Builder {
        private ValidationTypes type;
        private String expected;
        private String actual;

        public DocumentValidations.Builder type(ValidationTypes type) {
            this.type = type;
            return this;
        }

        public DocumentValidations.Builder expected(String expected) {
            this.expected = expected;
            return this;
        }

        public DocumentValidations.Builder actual(String actual) {
            this.actual = actual;
            return this;
        }

        public DocumentValidations create() {
            return new DocumentValidations(this);
        }

    }
}
