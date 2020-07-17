package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentDetails {

    private String desc;

    public String getDescription() {
        return description;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    @JsonCreator
    public DocumentDetails(@JsonProperty("desc") String desc) {
        this.desc = desc;
    }
}
