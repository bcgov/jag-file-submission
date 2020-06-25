package ca.bc.gov.open.jag.efilingaccountclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;

@ConfigurationProperties(prefix = "jag.efiling.account.client")
public class CsoAccountProperties extends EfilingSoapClientProperties {
}
