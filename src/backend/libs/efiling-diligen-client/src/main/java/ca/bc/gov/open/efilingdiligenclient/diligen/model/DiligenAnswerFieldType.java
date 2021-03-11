package ca.bc.gov.open.efilingdiligenclient.diligen.model;

import java.util.List;

public class DiligenAnswerFieldType {
    private String type;
    private boolean multi;
    private List<String> options;

    public String getType() {  return type;  }

    public boolean isMulti() {  return multi;  }

    public List<String> getOptions() {  return options;  }

    public DiligenAnswerFieldType(DiligenAnswerFieldType.Builder builder) {
        this.type = builder.type;
        this.multi = builder.multi;
        this.options = builder.options;
    }

    public static DiligenAnswerFieldType.Builder builder() {
        return new DiligenAnswerFieldType.Builder();
    }

    public static class Builder {

        private String type;
        private boolean multi;
        private List<String> options;


        public DiligenAnswerFieldType.Builder type(String type) {
            this.type = type;
            return this;
        }

        public DiligenAnswerFieldType.Builder multi(Boolean multi) {
            this.multi = multi;
            return this;
        }

        public DiligenAnswerFieldType.Builder options(List<String> options) {
            this.options = options;
            return this;
        }

        public DiligenAnswerFieldType create() {
            return new DiligenAnswerFieldType(this);
        }
    }

}
