package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class PaymentProfile {

    private BigDecimal code;
    private Integer approved;
    private String message;
    private String id;

    public PaymentProfile(BigDecimal code, Integer approved, String message, String id) {
        this.code = code;
        this.approved = approved;
        this.message = message;
        this.id = id;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
