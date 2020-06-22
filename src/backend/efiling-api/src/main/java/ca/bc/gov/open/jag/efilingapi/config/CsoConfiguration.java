package ca.bc.gov.open.jag.efilingapi.config;

import ca.bc.gov.open.jag.efilingaccountclient.DemoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class CsoConfiguration {

    private final AppProperties appProperties;

    public CsoConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    @ConditionalOnProperty("jag.efiling.global")
    public EfilingAccountService demoAccountService() {
        return new DemoAccountServiceImpl();
    }

}
