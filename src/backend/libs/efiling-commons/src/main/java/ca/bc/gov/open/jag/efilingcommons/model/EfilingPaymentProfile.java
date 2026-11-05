package ca.bc.gov.open.jag.efilingcommons.model;

public class EfilingPaymentProfile {

    private String code;
    private String name;
    private String profileId;

    public EfilingPaymentProfile(String token, String name, String profileId) {
        this.code = token;
        this.name = name;
        this.profileId = profileId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

}
