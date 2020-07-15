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
            @JsonProperty("udpDtm") String udpDtm,
            @JsonProperty("feeAmt") BigDecimal feeAmt,
            @JsonProperty("entUserId") String entUserId,
            @JsonProperty("serviceTypeCd") String serviceTypeCd,
            @JsonProperty("effectiveDt") String effectiveDt,
            @JsonProperty("updUserId") String updUserId,
            @JsonProperty("entDtm") String entDtm,
            @JsonProperty("expiryDt") String expiryDt) {

        this.udpDtm = udpDtm;
        this.feeAmt = feeAmt;
        this.entUserId = entUserId;
        this.serviceTypeCd = serviceTypeCd;
        this.effectiveDt = effectiveDt;
        this.updUserId = updUserId;
        this.entDtm = entDtm;
        this.expiryDt = expiryDt;
    }

    public String getUdpDtm() {
        return udpDtm;
    }

    public void setUdpDtm(String udpDtm) {
        this.udpDtm = udpDtm;
    }

    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public String getServiceTypeCd() {
        return serviceTypeCd;
    }

    public void setServiceTypeCd(String serviceTypeCd) {
        this.serviceTypeCd = serviceTypeCd;
    }

    public String getEffectiveDt() {
        return effectiveDt;
    }

    public void setEffectiveDt(String effectiveDt) {
        this.effectiveDt = effectiveDt;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public String getEntDtm() {
        return entDtm;
    }

    public void setEntDtm(String entDtm) {
        this.entDtm = entDtm;
    }

    public String getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(String expiryDt) {
        this.expiryDt = expiryDt;
    }

    String udpDtm;
    BigDecimal feeAmt;
    String entUserId;
    String serviceTypeCd;
    String effectiveDt;
    String updUserId;
    String entDtm;
    String expiryDt;
}
