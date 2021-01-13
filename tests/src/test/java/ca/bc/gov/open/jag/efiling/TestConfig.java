package ca.bc.gov.open.jag.efiling;

import ca.bc.gov.open.jag.efiling.services.OauthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public OauthService oauthService () {
        return new OauthService();
    }
}
