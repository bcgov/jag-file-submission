package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Party {

    private BigDecimal partyId;
    private String partyTypeCd;
    private String roleTypeCd;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameTypeCd;

    @JsonCreator
    public Party(
            @JsonProperty("partyId") BigDecimal partyId,
            @JsonProperty("partyTypeCd") String partyTypeCd,
            @JsonProperty("roleTypeCd") String roleTypeCd,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("nameTypeCd") String nameTypeCd) {
        this.partyId = partyId;
        this.partyTypeCd = partyTypeCd;
        this.roleTypeCd = roleTypeCd;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.nameTypeCd = nameTypeCd;
    }

    public Party(Builder builder) {
        this.partyId = builder.partyId;
        this.partyTypeCd = builder.partyTypeCd;
        this.roleTypeCd = builder.roleTypeCd;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.nameTypeCd = builder.nameTypeCd;
    }

    public BigDecimal getPartyId() {
        return partyId;
    }

    public String getPartyTypeCd() {
        return partyTypeCd;
    }

    public String getRoleTypeCd() {
        return roleTypeCd;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNameTypeCd() {
        return nameTypeCd;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BigDecimal partyId;
        private String partyTypeCd;
        private String roleTypeCd;
        private String firstName;
        private String middleName;
        private String lastName;
        private String nameTypeCd;

        public Builder partyId(BigDecimal partyId) {
            this.partyId = partyId;
            return this;
        }

        public Builder partyTypeCd(String partyTypeCd) {
            this.partyTypeCd = partyTypeCd;
            return this;
        }

        public Builder roleTypeCd(String roleTypeCd) {
            this.roleTypeCd = roleTypeCd;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder nameTypeCd(String nameTypeCd) {
            this.nameTypeCd = nameTypeCd;
            return this;
        }

        public Party create() {
            return new Party(this);
        }

    }

}
