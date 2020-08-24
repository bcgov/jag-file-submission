package ca.bc.gov.open.jag.efilingcsostarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cso")
public class CsoProperties {

    private String fileServerHost;

    private boolean debugEnabled = false;

    public String getFileServerHost() {
        return fileServerHost;
    }

    public void setFileServerHost(String fileServerHost) {

        this.fileServerHost = fileServerHost;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
