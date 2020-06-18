package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.open.jag.efilingaccountclient.DemoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {EfilingAccountService.class})
    public EfilingAccountService eFilingAccountService() {

        EfilingAccountService eFilingAccountService = new DemoAccountServiceImpl();
        return eFilingAccountService;
    }
}
