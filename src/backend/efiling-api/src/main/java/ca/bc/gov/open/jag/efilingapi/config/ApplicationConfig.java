package ca.bc.gov.open.jag.efilingapi.config;

import ca.bc.gov.open.jag.efilinglookupclient.DemoLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EfilingLookupService efilingLookupService() {
        return new DemoLookupServiceImpl();
    }

}
