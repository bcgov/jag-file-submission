package ca.bc.gov.open.jag.efilinglookupclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.*;

import java.math.BigDecimal;


public class ServiceFees {

    @SuppressWarnings("squid:S00107")
    @JsonCreator
    public ServiceFees(
            @JsonProperty("udpDtm") DateTime udpDtm,
            @JsonProperty("feeAmt") BigDecimal feeAmt,
            @JsonProperty("entUserId") String entUserId,
            @JsonProperty("serviceTypeCd") String serviceTypeCd,
            @JsonProperty("effectiveDt") DateTime effectiveDt,
            @JsonProperty("updUserId") String updUserId,
            @JsonProperty("entDtm") DateTime entDtm,
            @JsonProperty("expiryDt") DateTime expiryDt) {

        this.udpDtm = udpDtm;
        this.feeAmt = feeAmt;
        this.entUserId = entUserId;
        this.serviceTypeCd = serviceTypeCd;
        this.effectiveDt = effectiveDt;
        this.updUserId = updUserId;
        this.entDtm = entDtm;
        this.expiryDt = expiryDt;
    }

    public DateTime getUdpDtm() {
        return udpDtm;
    }

    public void setUdpDtm(DateTime udpDtm) {
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

    public DateTime getEffectiveDt() {
        return effectiveDt;
    }

    public void setEffectiveDt(DateTime effectiveDt) {
        this.effectiveDt = effectiveDt;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public DateTime getEntDtm() {
        return entDtm;
    }

    public void setEntDtm(DateTime entDtm) {
        this.entDtm = entDtm;
    }

    public DateTime getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(DateTime expiryDt) {
        this.expiryDt = expiryDt;
    }

    DateTime udpDtm;
    BigDecimal feeAmt;
    String entUserId;
    String serviceTypeCd;
    DateTime effectiveDt;
    String updUserId;
    DateTime entDtm;
    DateTime expiryDt;
}
