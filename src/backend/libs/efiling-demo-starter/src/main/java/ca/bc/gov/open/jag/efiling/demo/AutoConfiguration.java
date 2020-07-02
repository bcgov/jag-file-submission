package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    @Bean
    public EfilingAccountService efilingAccountService() {
        return new EfilingAccountServiceDemoImpl();
    }

    @Bean
    public EfilingLookupService efilingLookupService() {
        return new EfilingLookupServiceDemoImpl();
    }

}
