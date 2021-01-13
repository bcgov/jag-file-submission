package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                public AccountDetails getCsoAccountDetails(UUID universalId) {
                    return null;
                }

                @Override
                public void updateClient(AccountDetails accountDetails) {

                }

                @Override
                public AccountDetails createAccount(UUID universalId, CreateCsoAccountRequest createAccountRequest) {
                    return null;
                }
            };
        }

        @Bean
        public EfilingStatusService efilingStatusService() {
            return new EfilingStatusService() {
                @Override
                public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {
                    return Optional.empty();
                }

                @Override
                public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
                    return new ArrayList<>();
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
