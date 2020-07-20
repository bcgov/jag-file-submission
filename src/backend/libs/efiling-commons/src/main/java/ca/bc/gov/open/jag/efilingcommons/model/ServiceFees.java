package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.*;

import java.math.BigDecimal;


public class ServiceFees {

    // Allowing 8 params to constructor as it is a map to SOAP reply
    @SuppressWarnings("squid:S00107")
    @JsonCreator
    public ServiceFees(
            @JsonProperty("feeAmt") BigDecimal feeAmt,
            @JsonProperty("serviceTypeCd") String serviceTypeCd) {
        this.feeAmt = feeAmt;
        this.serviceTypeCd = serviceTypeCd;
    }


    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getServiceTypeCd() {
        return serviceTypeCd;
    }

    public void setServiceTypeCd(String serviceTypeCd) {
        this.serviceTypeCd = serviceTypeCd;
    }

    BigDecimal feeAmt;
    String serviceTypeCd;
}
