package ca.bc.gov.open.efilingdiligenclient.diligen.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormData {

    Map<String, FormDataProperty> properties = new LinkedHashMap<>();

    public Map<String, FormDataProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, FormDataProperty> properties) {
        this.properties = properties;
    }

}
