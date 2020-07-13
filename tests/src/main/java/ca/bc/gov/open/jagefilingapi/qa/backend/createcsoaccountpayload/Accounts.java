package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

public class Accounts {

    private String type;
    private String identifier;

    public Accounts(String type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
