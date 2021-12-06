package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.*;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidatorImpl;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionConfig {

    private final CacheProperties cacheProperties;
    private final NavigationProperties navigationProperties;

    public SubmissionConfig(CacheProperties cacheProperties, NavigationProperties navigationProperties) {
        this.cacheProperties = cacheProperties;
        this.navigationProperties = navigationProperties;
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
    public RushProcessingMapper rushProcessingMapper() { return new RushProcessingMapperImpl(); }

    @Bean
    public PartyMapper partyMapper() { return new PartyMapperImpl(); }

    @Bean
    public SubmissionService submissionService(SubmissionStore submissionStore,
                                               SubmissionMapper submissionMapper,
                                               EfilingLookupService efilingLookupService,
                                               EfilingCourtService efilingCourtService,
                                               EfilingSubmissionService efilingSubmissionService,
                                               EfilingDocumentService efilingDocumentService,
                                               DocumentStore documentStore,
                                               PaymentAdapter paymentAdapter,
                                               SftpService sftpService, PartyMapper partyMapper) {

        return new SubmissionServiceImpl(submissionStore,
                cacheProperties,
                submissionMapper,
                partyMapper,
                efilingLookupService,
                efilingCourtService,
                efilingSubmissionService,
                efilingDocumentService, documentStore,
                paymentAdapter,
                sftpService,
                navigationProperties);
    }

    @Bean
    public GenerateUrlRequestValidator packageValidator(SubmissionService submissionService, CourtService courtService, DocumentService documentService, FilingPackageService filingPackageService) {
        return new GenerateUrlRequestValidatorImpl(submissionService, courtService, documentService, filingPackageService);
    }

}
