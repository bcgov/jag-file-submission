package ca.bc.gov.open.jag.efilingcsoclient.config;

public class CsoProperties {

    private String fileServerHost;

    private String csoBasePath;

    private String csoPackagePath;

    private boolean debugEnabled = false;

    public String getFileServerHost() {
        return fileServerHost;
    }

    public void setFileServerHost(String fileServerHost) {
        this.fileServerHost = fileServerHost;
    }

    public String getCsoBasePath() {
        return csoBasePath;
    }

    public void setCsoBasePath(String csoBasePath) {
        this.csoBasePath = csoBasePath;
    }

    public String getCsoPackagePath() { return csoPackagePath; }

    public void setCsoPackagePath(String csoPackagePath) {  this.csoPackagePath = csoPackagePath; }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
