package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.role.client")
public class CsoRoleProperties extends EfilingSoapClientProperties {

}
