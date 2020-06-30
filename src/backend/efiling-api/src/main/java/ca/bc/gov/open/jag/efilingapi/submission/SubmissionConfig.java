package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    @Bean
    public SubmissionStore submissionStore() {
        return new SubmissionStoreImpl();
    }

    @Bean
    public SubmissionMapper submissionMapper() {
        return new SubmissionMapperImpl();
    }

    @Bean
    public SubmissionService submissionService(SubmissionStore submissionStore,
                                               CacheProperties cacheProperties,
                                               SubmissionMapper submissionMapper,
                                               EfilingAccountService efilingAccountService,
                                               FeeService feeService) {
        return new SubmissionServiceImpl(submissionStore,
                cacheProperties,
                submissionMapper,
                efilingAccountService,
                feeService);
    }

}
