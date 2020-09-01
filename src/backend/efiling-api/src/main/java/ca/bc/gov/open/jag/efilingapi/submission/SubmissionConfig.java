package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.*;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
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
    public FilingPackageMapper filingPackageApiMapper() { return new FilingPackageMapperImpl(); }

    @Bean
    public PartyMapper partyMapper() { return new PartyMapperImpl(); }

    @Bean
    public SubmissionService submissionService(SubmissionStore submissionStore,
                                               SubmissionMapper submissionMapper,
                                               EfilingLookupService efilingLookupService,
                                               EfilingCourtService efilingCourtService,
                                               EfilingSubmissionService efilingSubmissionService,
                                               DocumentStore documentStore,
                                               BamboraPaymentAdapter bamboraPaymentAdapter,
                                               SftpService sftpService, PartyMapper partyMapper) {

        return new SubmissionServiceImpl(submissionStore,
                cacheProperties,
                submissionMapper,
                partyMapper,
                efilingLookupService,
                efilingCourtService,
                efilingSubmissionService,
                documentStore,
                bamboraPaymentAdapter,
                sftpService);
    }

}
