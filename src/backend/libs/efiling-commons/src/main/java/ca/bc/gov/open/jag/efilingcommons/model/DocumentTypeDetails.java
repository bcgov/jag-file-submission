package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DocumentTypeDetails {

    private String description;

    private String type;

    private Boolean isAutoProcessing;

    private BigDecimal statutoryFeeAmount;

    private Boolean isOrderDocument;

    private Boolean rushRequired;

    public String getDescription() { return description; }

    public void setDescription(String description) {  this.description = description; }

    public BigDecimal getStatutoryFeeAmount() { return statutoryFeeAmount; }

    public void setStatutoryFeeAmount(BigDecimal statutoryFeeAmount) {  this.statutoryFeeAmount = statutoryFeeAmount;  }

    public Boolean getOrderDocument() { return isOrderDocument; }

    public void setOrderDocument(Boolean orderDocument) {  isOrderDocument = orderDocument;  }

    public Boolean isRushRequired() {
        return rushRequired;
    }

    public void setRushRequired(Boolean rushRequired) {
        this.rushRequired = rushRequired;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type;  }

    public Boolean isAutoProcessing() { return isAutoProcessing; }

    public void setAutoProcessing(Boolean autoProcessing) {  isAutoProcessing = autoProcessing; }

    @JsonCreator
    public DocumentTypeDetails(@JsonProperty("description") String description,
                               @JsonProperty("type") String type,
                               @JsonProperty("statutoryFeeAmount") BigDecimal statutoryFeeAmount,
                               @JsonProperty("isOrderDocument") boolean isOrderDocument,
                               @JsonProperty("rushRequired") boolean rushRequired,
                               @JsonProperty("isAutoProcessing") boolean isAutoProcessing) {
        this.description = description;
        this.type = type;
        this.statutoryFeeAmount = statutoryFeeAmount;
        this.isOrderDocument = isOrderDocument;
        this.rushRequired = rushRequired;
        this.isAutoProcessing = isAutoProcessing;
    }

}
