package ca.bc.gov.open.jag.efilingworker.configuration;

import ca.bc.gov.open.jag.efilingsubmissionclient.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingsubmissionclient.MockCSOSubmissionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EfilingWorkerConfig {
    @Bean
    public EfilingSubmissionService efilingSubmissionService() {
        return new MockCSOSubmissionServiceImpl();
    }
}
