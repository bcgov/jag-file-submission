package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.ag.csows.LookupsLookupFacade;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CSOLookupProperties.class)
public class AutoConfiguration {

    private final CSOLookupProperties csoLookupProperties;

    public AutoConfiguration(CSOLookupProperties csoLookupProperties) {
        this.csoLookupProperties = csoLookupProperties;
    }

    @Bean
    public LookupsLookupFacade eFilingLookupService() {
        LookupsLookupFacade lookupsLookupFacade = new LookupsLookupFacade();
        return lookupsLookupFacade;
    }
}
