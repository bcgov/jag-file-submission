package ca.bc.gov.open.jag.efilingcsostarter.config;

import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cso")
public class SpringCsoProperties extends CsoProperties {
}
