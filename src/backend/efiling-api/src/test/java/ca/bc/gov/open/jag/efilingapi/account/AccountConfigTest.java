package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountConfigTest {

    @Configuration
    public static class TestConfig {

        @Bean
        public EfilingAccountService efilingAccountService() {
            return new EfilingAccountService() {
                @Override
                public AccountDetails getAccountDetails(String userGuid) {
                    return null;
                }

                @Override
                public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {
                    return null;
                }

                @Override
                public void updateClient(AccountDetails accountDetails) {

                }

                @Override
                public String getOrderNumber() {
                    return null;
                }
            };
        }

    }


    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withUserConfiguration(AccountConfig.class);

    @Test
    public void testConfigure() throws Exception {

        context.run(it -> {
            assertThat(it).hasSingleBean(AccountService.class);
            assertThat(it).hasSingleBean(CsoAccountMapper.class);

            Assertions.assertEquals(CsoAccountMapperImpl.class, it.getBean(CsoAccountMapper.class).getClass());

        });

    }

}
