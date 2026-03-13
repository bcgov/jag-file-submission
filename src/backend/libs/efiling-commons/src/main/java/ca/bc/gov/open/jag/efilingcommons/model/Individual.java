package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Individual {

    private String partyTypeDesc;
    private String roleTypeCd;
    private String roleTypeDesc;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameTypeCd;

    @JsonCreator
    public Individual(
            @JsonProperty("partyTypeDesc") String partyTypeDesc,
            @JsonProperty("roleTypeCd") String roleTypeCd,
            @JsonProperty("roleTypeDesc") String roleTypeDesc,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("nameTypeCd") String nameTypeCd) {
        this.partyTypeDesc = partyTypeDesc;
        this.roleTypeCd = roleTypeCd;
        this.roleTypeDesc = roleTypeDesc;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.nameTypeCd = nameTypeCd;
    }

    public Individual(Builder builder) {
        this.partyTypeDesc = builder.partyTypeDesc;
        this.roleTypeCd = builder.roleTypeCd;
        this.roleTypeDesc = builder.roleTypeDesc;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.nameTypeCd = builder.nameTypeCd;
    }

    public String getPartyTypeDesc() {
        return partyTypeDesc;
    }

    public String getRoleTypeCd() {
        return roleTypeCd;
    }

    public String getRoleTypeDesc() {
        return roleTypeDesc;
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

        private String partyTypeDesc;
        private String roleTypeCd;
        private String roleTypeDesc;
        private String firstName;
        private String middleName;
        private String lastName;
        private String nameTypeCd;

        public Builder partyTypeDesc(String partyTypeDesc) {
            this.partyTypeDesc = partyTypeDesc;
            return this;
        }

        public Builder roleTypeCd(String roleTypeCd) {
            this.roleTypeCd = roleTypeCd;
            return this;
        }

        public Builder roleTypeDesc(String roleTypeDesc) {
            this.roleTypeDesc = roleTypeDesc;
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

        public Individual create() {
            return new Individual(this);
        }

    }

}
