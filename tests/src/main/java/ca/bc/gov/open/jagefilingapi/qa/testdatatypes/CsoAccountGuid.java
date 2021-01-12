package ca.bc.gov.open.jagefilingapi.qa.testdatatypes;

public class CsoAccountGuid {

    private String validExistingCSOGuid;
    private String invalidNoFilingRoleGuid;
    private String validUserId;

    public CsoAccountGuid() {
        // Empty constructor
    }

    public String getValidExistingCSOGuid() {
        return validExistingCSOGuid;
    }

    public void setValidExistingCSOGuid(String validExistingCSOGuid) {
        this.validExistingCSOGuid = validExistingCSOGuid;
    }

    public String getInvalidNoFilingRoleGuid() {
        return invalidNoFilingRoleGuid;
    }

    public void setInvalidNoFilingRoleGuid(String invalidNoFilingRoleGuid) {
        this.invalidNoFilingRoleGuid = invalidNoFilingRoleGuid;
    }

    public String getValidUserId() {
        return validUserId;
    }

    public void setValidUserId(String validUserId) {
        this.validUserId = validUserId;
    }
}
