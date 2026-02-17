package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Organization {
    private String partyTypeDesc;
    private String roleTypeCd;
    private String roleTypeDesc;
    private String name;
    private String nameTypeCd;

    @JsonCreator
    public Organization(
            @JsonProperty("partyTypeDesc") String partyTypeDesc,
            @JsonProperty("roleTypeCd") String roleTypeCd,
            @JsonProperty("roleTypeDesc") String roleTypeDesc,
            @JsonProperty("name") String name,
            @JsonProperty("nameTypeCd") String nameTypeCd) {
        this.partyTypeDesc = partyTypeDesc;
        this.roleTypeCd = roleTypeCd;
        this.roleTypeDesc = roleTypeDesc;
        this.name = name;
        this.nameTypeCd = nameTypeCd;
    }

    public Organization(Organization.Builder builder) {
        this.partyTypeDesc = builder.partyTypeDesc;
        this.roleTypeCd = builder.roleTypeCd;
        this.roleTypeDesc = builder.roleTypeDesc;
        this.name = builder.name;
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

    public String getName() {
        return name;
    }

    public String getNameTypeCd() {
        return nameTypeCd;
    }

    public static Organization.Builder builder() {
        return new Organization.Builder();
    }

    public static class Builder {

        private String partyTypeDesc;
        private String roleTypeCd;
        private String roleTypeDesc;
        private String name;
        private String nameTypeCd;

        public Organization.Builder partyTypeDesc(String partyTypeDesc) {
            this.partyTypeDesc = partyTypeDesc;
            return this;
        }

        public Organization.Builder roleTypeCd(String roleTypeCd) {
            this.roleTypeCd = roleTypeCd;
            return this;
        }

        public Organization.Builder roleTypeDesc(String roleTypeDesc) {
            this.roleTypeDesc = roleTypeDesc;
            return this;
        }

        public Organization.Builder name(String name) {
            this.name = name;
            return this;
        }

        public Organization.Builder nameTypeCd(String nameTypeCd) {
            this.nameTypeCd = nameTypeCd;
            return this;
        }

        public Organization create() {
            return new Organization(this);
        }
    }
}
