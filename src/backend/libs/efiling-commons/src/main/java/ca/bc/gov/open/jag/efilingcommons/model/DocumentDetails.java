package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DocumentDetails {

    private String description;

    private BigDecimal statutoryFeeAmount;

    public String getDescription() { return description; }

    public void setDescription(String description) {  this.description = description; }

    public BigDecimal getStatutoryFeeAmount() { return statutoryFeeAmount; }

    public void setStatutoryFeeAmount(BigDecimal statutoryFeeAmount) {  this.statutoryFeeAmount = statutoryFeeAmount;  }

    @JsonCreator
    public DocumentDetails(@JsonProperty("description") String description, @JsonProperty("statutoryFeeAmount") BigDecimal statutoryFeeAmount) {
        this.description = description;
        this.statutoryFeeAmount = statutoryFeeAmount;
    }
}
