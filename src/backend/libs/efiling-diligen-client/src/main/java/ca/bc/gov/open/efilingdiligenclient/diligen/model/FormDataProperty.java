package ca.bc.gov.open.efilingdiligenclient.diligen.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormDataProperty {

    private String type;
    private Integer fieldId;
    Map<String, FormDataProperty> properties = new LinkedHashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Map<String, FormDataProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, FormDataProperty> properties) {
        this.properties = properties;
    }
}
