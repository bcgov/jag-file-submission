package ca.bc.gov.open.jag.efilinglookupclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.lookup.client")
public class CSOLookupProperties {

    private String filingLookupSoapUri;

    public String getFilingLookupSoapUri() {
        return filingLookupSoapUri;
    }

    public void setFilingLookupSoapUri(String filingLookupSoapUri) {
        this.filingLookupSoapUri = filingLookupSoapUri;
    }
}
