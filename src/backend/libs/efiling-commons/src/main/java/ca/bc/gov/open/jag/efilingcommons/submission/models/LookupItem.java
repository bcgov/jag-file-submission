package ca.bc.gov.open.jag.efilingcommons.submission.models;

public class LookupItem {
    private String code;
    private String description;


    public String getCode() { return code; }

    public String getDescription() { return description; }

    protected LookupItem(LookupItem.Builder builder) {

        this.code = builder.code;
        this.description = builder.description;

    }

    public static LookupItem.Builder builder() {

        return new LookupItem.Builder();

    }

    public static class Builder {

        private String code;
        private String description;

        public LookupItem.Builder code(String code) {
            this.code = code;
            return this;
        }

        public LookupItem.Builder description(String description) {
            this.description = description;
            return this;
        }

        public LookupItem create() {
            return new LookupItem(this);
        }

    }

}
