package ca.bc.gov.open.jag.ceis;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ceis")
public class CeisProperties {
    private String ceisBasePath;
    private String ceisUsername;
    private String ceisPassword;

    public String getCeisBasePath() {
        return ceisBasePath;
    }

    public void setCeisBasePath(String ceisBasePath) {
        this.ceisBasePath = ceisBasePath;
    }

    public String getCeisUsername() {
        return ceisUsername;
    }

    public void setCeisUsername(String ceisUsername) {
        this.ceisUsername = ceisUsername;
    }

    public String getCeisPassword() {
        return ceisPassword;
    }

    public void setCeisPassword(String ceisPassword) {
        this.ceisPassword = ceisPassword;
    }
}
