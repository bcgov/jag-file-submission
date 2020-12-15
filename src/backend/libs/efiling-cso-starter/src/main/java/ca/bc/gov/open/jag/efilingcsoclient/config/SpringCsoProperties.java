package ca.bc.gov.open.jag.efilingcsoclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cso")
public class SpringCsoProperties extends CsoProperties {
}
