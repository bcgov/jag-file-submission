package ca.bc.gov.open.jag.efilingaccountclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.account.client")
public class CsoAccountProperties {
    private String userName;
    private String password;
    private String filingAccountSoapUri;
    private String filingRoleSoapUri;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilingAccountSoapUri() {
        return filingAccountSoapUri;
    }

    public void setFilingAccountSoapUri(String filingAccountSoapUri) {
        this.filingAccountSoapUri = filingAccountSoapUri;
    }

    public String getFilingRoleSoapUri() {
        return filingRoleSoapUri;
    }

    public void setFilingRoleSoapUri(String filingRoleSoapUri) {
        this.filingRoleSoapUri = filingRoleSoapUri;
    }
}
