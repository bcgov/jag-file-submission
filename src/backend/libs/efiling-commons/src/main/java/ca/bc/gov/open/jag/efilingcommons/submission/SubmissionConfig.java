package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.document.DocumentStore;
import ca.bc.gov.open.jag.efilingcommons.soap.service.*;
import ca.bc.gov.open.jag.efilingcommons.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingcommons.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingcommons.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.submission.service.SubmissionStoreImpl;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    private final CacheProperties cacheProperties;

    public SubmissionConfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

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
                                               SubmissionMapper submissionMapper,
                                               EfilingAccountService efilingAccountService,
                                               EfilingLookupService efilingLookupService,
                                               EfilingCourtService efilingCourtService,
                                               EfilingSubmissionService efilingSubmissionService,
                                               DocumentStore documentStore) {

        return new SubmissionServiceImpl(submissionStore,
                cacheProperties,
                submissionMapper,
                efilingAccountService,
                efilingLookupService,
                efilingCourtService,
                efilingSubmissionService,
                documentStore);
    }

}
