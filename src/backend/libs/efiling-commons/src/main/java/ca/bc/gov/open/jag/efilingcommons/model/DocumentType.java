package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentType {
    
    private String description;

    private String type;

    private Boolean rushRequired;

    public String getDescription() { return description; }

    public void setDescription(String description) {  this.description = description; }

    public String getType() { return this.type; }

    public void setType(String type) {  this.type = type;  }

    public Boolean isRushRequired() { return rushRequired; }

    public void setRushRequired(Boolean rushRequired) { this.rushRequired = rushRequired; }

    @JsonCreator
    public DocumentType(@JsonProperty("description") String description,
                        @JsonProperty("type") String type,
                        @JsonProperty("rushRequired") Boolean rushRequired) {
        this.description = description;
        this.type = type;
        this.rushRequired = rushRequired;
    }
}
