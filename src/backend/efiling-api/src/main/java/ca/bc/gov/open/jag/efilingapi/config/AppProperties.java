package ca.bc.gov.open.jag.efilingapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.global")
public class AppProperties {

    private boolean demo;

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }
}
