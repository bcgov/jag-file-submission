package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {EfilingAccountService.class})
    public EfilingAccountService eFilingAccountService() {

        return new EfilingAccountServiceImpl();
    }
}
