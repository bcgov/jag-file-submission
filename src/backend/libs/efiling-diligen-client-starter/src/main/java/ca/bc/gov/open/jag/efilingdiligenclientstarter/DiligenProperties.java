package ca.bc.gov.open.jag.efilingdiligenclientstarter;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the Diligen Configuration
 */
@ConfigurationProperties(prefix = "jag.efiling.diligen")
public class DiligenProperties {

    public String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

}
