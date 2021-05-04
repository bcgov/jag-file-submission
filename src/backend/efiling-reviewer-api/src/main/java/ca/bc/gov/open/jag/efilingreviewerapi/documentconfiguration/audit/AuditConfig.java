package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * A simple configuration that enables auditing in Mongo (auto-population of
 * <code>@CreatedDate</code>, <code>@LastModifiedDate</code>, and
 * <code>@Version</code> fields.
 */
@Configuration
@EnableMongoAuditing
public class AuditConfig {

}
