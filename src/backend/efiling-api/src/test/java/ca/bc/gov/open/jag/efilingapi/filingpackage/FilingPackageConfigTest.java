package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageConfigTest")
public class FilingPackageConfigTest {


    @Configuration
    public static class TestConfig {

        @Bean
        public AccountService accountService() {
            return new AccountService() {

                @Override
                public AccountDetails getCsoAccountDetails(String universalId) {
                    return null;
                }

                @Override
                public void updateClient(AccountDetails accountDetails) {

                }

                @Override
                public AccountDetails createAccount(String universalId, String identityProvider ,CreateCsoAccountRequest createAccountRequest) {
                    return null;
                }
            };
        }

        @Bean
        public EfilingDocumentService efilingDocumentService() {
            return new EfilingDocumentService() {
                @Override
                public DocumentTypeDetails getDocumentTypeDetails(String courtLevel, String courtClass, String documentType, String division) {
                    return null;
                }

                @Override
                public List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass, String division) {
                    return null;
                }
            };
        }

        @Bean
        public EfilingReviewService efilingReviewService() {
            return new EfilingReviewService() {
                @Override
                public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {
                    return Optional.empty();
                }

                @Override
                public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
                    return new ArrayList<>();
                }

                @Override
                public Optional<byte[]> getReport(ReportRequest reportRequest) {
                    return Optional.empty();
                }

                @Override
                public Optional<byte[]> getSubmittedDocument(BigDecimal documentIdentifier) {
                    return Optional.empty();
                }

                @Override
                public void deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest) {
                    //Do nothing
                }

                @Override
                public Optional<byte[]> getRushDocument(RushDocumentRequest rushDocumentRequest) {
                    return Optional.empty();
                }
            };

        }

    }

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(FilingPackageConfigTest.TestConfig.class)
            .withUserConfiguration(FilingPackageConfig.class);

    @Test
    public void testConfigure() {

        context.run(it -> {
            assertThat(it).hasSingleBean(AccountService.class);
            assertThat(it).hasSingleBean(FilingPackageService.class);

        });

    }



}
