package ca.bc.gov.open.jag.ceis;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ceis")
public class CeisProperties {
    private String ceisBasePath;

    public String getCeisBasePath() {
        return ceisBasePath;
    }

    public void setCeisBasePath(String ceisBasePath) {
        this.ceisBasePath = ceisBasePath;
    }
}
