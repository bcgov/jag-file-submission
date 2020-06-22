package ca.bc.gov.open.jag.efilingaccountclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.account.client")
public class CsoAccountProperties {

    private String filingAccountSoapUri;

    public String getFilingAccountSoapUri() {
        return filingAccountSoapUri;
    }

    public void setFilingAccountSoapUri(String filingAccountSoapUri) {
        this.filingAccountSoapUri = filingAccountSoapUri;
    }
}
