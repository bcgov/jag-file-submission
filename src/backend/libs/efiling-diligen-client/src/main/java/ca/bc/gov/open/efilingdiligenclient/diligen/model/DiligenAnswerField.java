package ca.bc.gov.open.efilingdiligenclient.diligen.model;

import java.util.List;

public class DiligenAnswerField {
    private Integer id;
    private String name;
    private Integer createdBy;
    private DiligenAnswerFieldType fieldType;
    private List<String> values;

    public Integer getId() {  return id;  }

    public String getName() {  return name;   }

    public Integer getCreatedBy() {  return createdBy;  }

    public DiligenAnswerFieldType getFieldType() {  return fieldType;  }

    public List<String> getValues() {  return values;  }

    public DiligenAnswerField(DiligenAnswerField.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.createdBy = builder.createdBy;
        this.fieldType = builder.fieldType;
        this.values = builder.values;
    }

    public static DiligenAnswerField.Builder builder() {
        return new DiligenAnswerField.Builder();
    }

    public static class Builder {

        private Integer id;
        private String name;
        private Integer createdBy;
        private DiligenAnswerFieldType fieldType;
        private List<String> values;


        public DiligenAnswerField.Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public DiligenAnswerField.Builder name(String name) {
            this.name = name;
            return this;
        }

        public DiligenAnswerField.Builder createdBy(Integer createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DiligenAnswerField.Builder fieldType(DiligenAnswerFieldType fieldType) {
            this.fieldType = fieldType;
            return this;
        }

        public DiligenAnswerField.Builder values(List<String> values) {
            this.values = values;
            return this;
        }

        public DiligenAnswerField create() {
            return new DiligenAnswerField(this);
        }
    }
}
