package ca.bc.gov.open.jag.efiling;

import ca.bc.gov.open.jag.efiling.services.CourtService;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import ca.bc.gov.open.jag.efiling.services.SubmissionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public OauthService oauthService () {
        return new OauthService();
    }

    @Bean
    public SubmissionService submissionService () {
        return new SubmissionService();
    }

    @Bean
    public CourtService courtService () {
        return new CourtService();
    }

}
