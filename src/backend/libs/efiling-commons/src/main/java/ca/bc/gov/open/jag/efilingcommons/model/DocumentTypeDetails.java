package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DocumentTypeDetails {

    private String description;

    private String type;

    private Boolean autoProcessing;

    private BigDecimal statutoryFeeAmount;

    private Boolean orderDocument;

    private Boolean rushRequired;

    public String getDescription() { return description; }

    public void setDescription(String description) {  this.description = description; }

    public BigDecimal getStatutoryFeeAmount() { return statutoryFeeAmount; }

    public void setStatutoryFeeAmount(BigDecimal statutoryFeeAmount) {  this.statutoryFeeAmount = statutoryFeeAmount;  }

    public Boolean isOrderDocument() { return orderDocument; }

    public void setOrderDocument(Boolean orderDocument) {  this.orderDocument = orderDocument;  }

    public Boolean isRushRequired() {
        return rushRequired;
    }

    public void setRushRequired(Boolean rushRequired) {
        this.rushRequired = rushRequired;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type;  }

    public Boolean isAutoProcessing() { return autoProcessing; }

    public void setAutoProcessing(Boolean autoProcessing) {  this.autoProcessing = autoProcessing; }

    @JsonCreator
    public DocumentTypeDetails(@JsonProperty("description") String description,
                               @JsonProperty("type") String type,
                               @JsonProperty("statutoryFeeAmount") BigDecimal statutoryFeeAmount,
                               @JsonProperty("orderDocument") boolean orderDocument,
                               @JsonProperty("rushRequired") boolean rushRequired,
                               @JsonProperty("autoProcessing") boolean autoProcessing) {
        this.description = description;
        this.type = type;
        this.statutoryFeeAmount = statutoryFeeAmount;
        this.orderDocument = orderDocument;
        this.rushRequired = rushRequired;
        this.autoProcessing = autoProcessing;
    }

}
