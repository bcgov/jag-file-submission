package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

public class CsoPayload {

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String internalClientNumber;

    public CsoPayload(String internalClientNumber) {
        this.internalClientNumber = internalClientNumber;
    }

    public CsoPayload(String firstName, String lastName,
                      String middleName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
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

    public String getInternalClientNumber() {
        return internalClientNumber;
    }

    public void setInternalClientNumber(String internalClientNumber) {
        this.internalClientNumber = internalClientNumber;
    }
}
