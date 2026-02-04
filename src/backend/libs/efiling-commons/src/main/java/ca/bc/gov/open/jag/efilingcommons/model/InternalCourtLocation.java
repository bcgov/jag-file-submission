package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class InternalCourtLocation {

    private BigDecimal id;

    private String identifierCode;

    private String name;

    private String code;

    private Boolean isSupremeCourt;

    private Address address;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsSupremeCourt() {
        return isSupremeCourt;
    }

    public void setIsSupremeCourt(Boolean isSupremeCourt) {
        this.isSupremeCourt = isSupremeCourt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getIdentifierCode() {
        return identifierCode;
    }

    public void setIdentifierCode(String identifierCode) {
        this.identifierCode = identifierCode;
    }
    
}
