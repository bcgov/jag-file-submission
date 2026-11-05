package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.account.mappers.*;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {

    @Bean
    public CreateAccountRequestMapper createAccountRequestMapper() {
        return new CreateAccountRequestMapperImpl();
    }

    @Bean
    public AccountService accountService(EfilingAccountService efilingAccountService, CreateAccountRequestMapper createAccountRequestMapper) {
        return new AccountServiceImpl(efilingAccountService, createAccountRequestMapper);
    }

    @Bean
    public BceidAccountMapper bceidAccountMapper() {
        return new BceidAccountMapperImpl();
    }

    @Bean
    public CsoAccountMapper csoAccountMapper() { return new CsoAccountMapperImpl(); }

}
