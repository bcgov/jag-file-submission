package ca.bc.gov.open.jag.packagereview.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "jag.efiling.soap.client.status")
public class StatusProperties {
    private String userName;
    private String password;
    private String uri;
    private boolean isDebugEnabled;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public void setIsDebugEnabled(boolean debugEnabled) {
        isDebugEnabled = debugEnabled;
    }
}
