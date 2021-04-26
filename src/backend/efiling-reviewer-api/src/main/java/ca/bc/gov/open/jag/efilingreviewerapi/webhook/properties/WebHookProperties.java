package ca.bc.gov.open.jag.efilingreviewerapi.webhook.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.cso.webhook" )
public class WebHookProperties {

    private String basePath;
    private String returnPath;

    public String getBasePath() { return basePath; }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getReturnPath() {  return returnPath;  }

    public void setReturnPath(String returnPath) { this.returnPath = returnPath; }
}
