package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class ClientApplication {

    private String displayName;
    private String type;

    public ClientApplication(String displayName, String type) {
        this.displayName = displayName;
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
