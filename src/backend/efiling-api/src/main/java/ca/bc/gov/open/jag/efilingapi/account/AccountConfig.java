package ca.bc.gov.open.jag.efilingapi.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.open.jag.efilingapi.account.mappers.BceidAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.BceidAccountMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CreateAccountRequestMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CreateAccountRequestMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;

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
