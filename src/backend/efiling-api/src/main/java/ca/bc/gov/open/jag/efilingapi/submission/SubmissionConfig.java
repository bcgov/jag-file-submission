package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingaccountclient.DemoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    @Bean
    public SubmissionService submissionService() {
        return new SubmissionServiceImpl();
    }


    @Bean
    public EfilingAccountService efilingAccountService() {
        return new DemoAccountServiceImpl();
    }

}
