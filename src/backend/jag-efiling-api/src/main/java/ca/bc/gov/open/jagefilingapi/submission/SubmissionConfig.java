package ca.bc.gov.open.jagefilingapi.submission;

import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    @Bean
    public SubmissionService submissionService() {
        return new SubmissionServiceImpl();
    }

}