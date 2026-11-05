package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


public class ServiceFees {


    // Allowing 8 params to constructor as it is a map to SOAP reply
    @SuppressWarnings("squid:S00107")
    @JsonCreator
    public ServiceFees(
            @JsonProperty("feeAmount") BigDecimal feeAmount,
            @JsonProperty("serviceTypeCd") String serviceTypeCd) {
        this.feeAmount = feeAmount;
        this.serviceTypeCd = serviceTypeCd;
    }


    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getServiceTypeCd() {
        return serviceTypeCd;
    }

    public void setServiceTypeCd(String serviceTypeCd) {
        this.serviceTypeCd = serviceTypeCd;
    }

    BigDecimal feeAmount;
    String serviceTypeCd;
}
