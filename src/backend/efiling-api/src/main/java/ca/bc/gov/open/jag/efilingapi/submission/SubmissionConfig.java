package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraProperties;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.EfilingFilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.EfilingFilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    private final CacheProperties cacheProperties;

    private BamboraPaymentAdapter bamboraPaymentAdapter;

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
    public EfilingFilingPackageMapper efilingFilingPackageMapper() {
        return new EfilingFilingPackageMapperImpl();
    }

    @Bean
    public SubmissionService submissionService(SubmissionStore submissionStore,
                                               SubmissionMapper submissionMapper,
                                               EfilingFilingPackageMapper efilingFilingPackageMapper,
                                               EfilingLookupService efilingLookupService,
                                               EfilingCourtService efilingCourtService,
                                               EfilingSubmissionService efilingSubmissionService,
                                               DocumentStore documentStore) {

        return new SubmissionServiceImpl(submissionStore,
                cacheProperties,
                submissionMapper,
                efilingFilingPackageMapper,
                efilingLookupService,
                efilingCourtService,
                efilingSubmissionService,
                documentStore,
                bamboraPaymentAdapter);
    }

}
