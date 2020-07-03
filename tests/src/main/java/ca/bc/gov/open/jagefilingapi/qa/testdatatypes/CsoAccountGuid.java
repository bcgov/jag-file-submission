package ca.bc.gov.open.jagefilingapi.qa.testdatatypes;

public class CsoAccountGuid {

    private String validExistingCSOGuid;
    private String invalidGuid;
    private String nonExistingCSOGuid;

    public String getValidExistingCSOGuid() {
        return validExistingCSOGuid;
    }

    public void setValidExistingCSOGuid(String validExistingCSOGuid) {
        this.validExistingCSOGuid = validExistingCSOGuid;
    }

    public String getInvalidGuid() {
        return invalidGuid;
    }

    public void setInvalidGuid(String invalidGuid) {
        this.invalidGuid = invalidGuid;
    }

    public String getNonExistingCSOGuid() {
        return nonExistingCSOGuid;
    }

    public void setNonExistingCSOGuid(String nonExistingCSOGuid) {
        this.nonExistingCSOGuid = nonExistingCSOGuid;
    }
}
