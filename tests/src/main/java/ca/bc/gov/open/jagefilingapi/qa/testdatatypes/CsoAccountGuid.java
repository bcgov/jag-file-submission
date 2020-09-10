package ca.bc.gov.open.jagefilingapi.qa.testdatatypes;

public class CsoAccountGuid {

    private String validExistingCSOGuid;
    private String invalidNoFilingRoleGuid;
    private String nonExistingCSOGuid;
    private String validUserId;
    private String inValidUserId;
    private String clientSecret;

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

    public String getNonExistingCSOGuid() {
        return nonExistingCSOGuid;
    }

    public void setNonExistingCSOGuid(String nonExistingCSOGuid) {
        this.nonExistingCSOGuid = nonExistingCSOGuid;
    }

    public String getValidUserId() {
        return validUserId;
    }

    public String getInValidUserId() {return inValidUserId;}

    public String getClientSecret() {
        return clientSecret;
    }
}
