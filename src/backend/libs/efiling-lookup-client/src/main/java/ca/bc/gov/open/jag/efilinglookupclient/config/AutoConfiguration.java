package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.open.jag.efilinglookupclient.DemoLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {EfilingLookupService.class})
    public EfilingLookupService eFilingLookupService() {

        EfilingLookupService eFilingLookupService = new DemoLookupServiceImpl();
        return eFilingLookupService;
    }
}
