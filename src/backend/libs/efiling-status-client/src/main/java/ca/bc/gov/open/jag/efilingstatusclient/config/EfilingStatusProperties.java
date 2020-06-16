package ca.bc.gov.open.jag.efilingstatusclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.status.client")
public class EfilingStatusProperties {

    private String filingStatusSoapUri;

    public String getFilingStatusSoapUri() {
        return filingStatusSoapUri;
    }

    public void setFilingStatusSoapUri(String filingStatusSoapUri) {
        this.filingStatusSoapUri = filingStatusSoapUri;
    }
}
