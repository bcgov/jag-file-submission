package ca.bc.gov.open.jag.efilingcommons.model;

/**
 * Represents a request to create an account
 */
public class CreateAccountRequest {

    private String universalId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;

    protected CreateAccountRequest(CreateAccountRequest.Builder builder) {

        this.universalId = builder.universalId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.middleName = builder.middleName;
        this.email = builder.email;

    }

    public static CreateAccountRequest.Builder builder() {
        return new CreateAccountRequest.Builder();
    }

    /**
     * Get the universalId
     * @return
     */
    public String getUniversalId() {
        return universalId;
    }

    /**
     * Get the account First Name
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the account Last Name
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the account Middle Name
     * @return
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Get the account Email
     * @return
     */
    public String getEmail() {
        return email;
    }

    public static class Builder {

        private String universalId;
        private String firstName;
        private String lastName;
        private String middleName;
        private String email;

        public Builder universalId(String universalId) {
            this.universalId = universalId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }


        public CreateAccountRequest create() {
            return new CreateAccountRequest(this);
        }

    }

}
