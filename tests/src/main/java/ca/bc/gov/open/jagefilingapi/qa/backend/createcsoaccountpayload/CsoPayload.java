package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

public class CsoPayload {

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    Accounts accounts;

    public CsoPayload(String firstName, String middleName, String lastName, String email, Accounts accounts) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Accounts getAccounts() { return accounts; }

    public void setAccounts(Accounts accounts) { this.accounts = accounts; }
}
