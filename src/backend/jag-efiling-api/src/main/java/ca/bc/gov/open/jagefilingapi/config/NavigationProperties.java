package ca.bc.gov.open.jagefilingapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * efiling api navigation properties
 */
@ConfigurationProperties(prefix = "jag.efiling.navigation")
public class NavigationProperties {

    private String baseUrl;

    /**
     * Returns the base url to create a secure upload url
     * @return
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
