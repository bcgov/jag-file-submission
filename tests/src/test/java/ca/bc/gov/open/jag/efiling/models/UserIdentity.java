package ca.bc.gov.open.jag.efiling.models;

public class UserIdentity {

    private String accessToken;
    private String universalId;

    public UserIdentity(String accessToken, String universalId) {
        this.accessToken = accessToken;
        this.universalId = universalId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUniversalId() {
        return universalId;
    }
}
