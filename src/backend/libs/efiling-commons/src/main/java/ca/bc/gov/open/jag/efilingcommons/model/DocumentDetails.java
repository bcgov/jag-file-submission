package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentDetails {

    private String desc;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @JsonCreator
    public DocumentDetails(@JsonProperty("description") String description) {
        this.description = description;
    }
}
