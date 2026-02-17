package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RushProcessing {

    private String rushType;
    private String firstName;
    private String lastName;
    private String email;
    private String organization;
    private String phoneNumber;
    private String courtDate;
    private String country;
    private String countryCode;
    private String reason;
    private List<Document> supportingDocuments = new ArrayList<>();

    public RushProcessing(RushProcessing.Builder builder) {
        this.rushType = builder.rushType;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.organization = builder.organization;
        this.phoneNumber = builder.phoneNumber;
        this.courtDate = builder.courtDate;
        this.country = builder.country;
        this.countryCode = builder.countryCode;
        this.reason = builder.reason;
        this.supportingDocuments.addAll(builder.supportingDocuments);
    }

    @JsonCreator
    public RushProcessing(
            @JsonProperty("rushType") String rushType,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("organization") String organization,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("courtDate") String courtDate,
            @JsonProperty("country") String country,
            @JsonProperty("countryCode") String countryCode,
            @JsonProperty("reason") String reason,
            @JsonProperty("supportingDocuments") List<Document> supportingDocuments) {
        this.rushType = rushType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organization = organization;
        this.phoneNumber = phoneNumber;
        this.courtDate = courtDate;
        this.country = country;
        this.countryCode = countryCode;
        this.reason = reason;
        this.supportingDocuments.addAll(supportingDocuments);
    }

    public String getRushType() { return rushType;  }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() { return email; }

    public String getOrganization() {
        return organization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCourtDate() {  return courtDate;  }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getReason() {
        return reason;
    }

    public List<Document> getSupportingDocuments() {
        return supportingDocuments;
    }

    public static RushProcessing.Builder builder() {
        return new RushProcessing.Builder();
    }

    public static class Builder {

        private String rushType;
        private String firstName;
        private String lastName;
        private String email;
        private String organization;
        private String phoneNumber;
        private String courtDate;
        private String country;
        private String countryCode;
        private String reason;
        private List<Document> supportingDocuments = new ArrayList<>();

        public RushProcessing.Builder rushType(String rushType) {
            this.rushType = rushType;
            return this;
        }

        public RushProcessing.Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RushProcessing.Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RushProcessing.Builder email(String email) {
            this.email = email;
            return this;
        }

        public RushProcessing.Builder organization(String organization) {
            this.organization = organization;
            return this;
        }

        public RushProcessing.Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public RushProcessing.Builder courtDate(String courtDate) {
            this.courtDate = courtDate;
            return this;
        }

        public RushProcessing.Builder country(String country) {
            this.country = country;
            return this;
        }

        public RushProcessing.Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public RushProcessing.Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RushProcessing.Builder supportingDocuments(List<Document> supportingDocuments) {
            this.supportingDocuments.addAll(supportingDocuments);
            return this;
        }

        public RushProcessing create() {
            return new RushProcessing(this);
        }

    }

}
