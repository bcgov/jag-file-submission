package ca.bc.gov.open.jag.efilingfacadeclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.client")
public class EfilingFacadeProperties {

    private String filingFacadeSoapUri;

    public String getFilingFacadeSoapUri() {
        return filingFacadeSoapUri;
    }

    public void setFilingFacadeSoapUri(String filingFacadeSoapUri) {
        this.filingFacadeSoapUri = filingFacadeSoapUri;
    }
}
