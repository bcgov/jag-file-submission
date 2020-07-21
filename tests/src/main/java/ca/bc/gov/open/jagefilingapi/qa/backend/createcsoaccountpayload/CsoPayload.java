package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

public class CsoPayload {

    private String universalId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    Accounts[] accounts;

    public CsoPayload(String universalId, String firstName, String lastName,
                      String middleName, String email, Accounts[] accounts) {

        this.universalId = universalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.accounts = accounts;
    }

    public String getUniversalId() {
        return universalId;
    }

    public void setUniversalId(String universalId) {
        this.universalId = universalId;
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

    public Accounts[] getAccounts() { return accounts; }

    public void setAccounts(Accounts[] accounts) { this.accounts = accounts; }
}
