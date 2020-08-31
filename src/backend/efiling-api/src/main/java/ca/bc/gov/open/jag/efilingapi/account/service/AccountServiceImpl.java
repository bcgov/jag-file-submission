package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingapi.account.mappers.CreateAccountRequestMapper;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final EfilingAccountService efilingAccountService;
    private final CreateAccountRequestMapper createAccountRequestMapper;

    public AccountServiceImpl(EfilingAccountService efilingAccountService, CreateAccountRequestMapper createAccountRequestMapper) {
        this.efilingAccountService = efilingAccountService;
        this.createAccountRequestMapper = createAccountRequestMapper;
    }

    @Override
    @Cacheable(cacheNames = "accountDetails", key = "#universalId", cacheManager = "accountDetailsCacheManager", unless = "#result == null || #result.getInternalClientNumber() == null")
    public AccountDetails getCsoAccountDetails(UUID universalId) {
        return efilingAccountService.getAccountDetails(universalId);
    }

    @Override
    public void updateClient(AccountDetails accountDetails) {
        efilingAccountService.updateClient(accountDetails);
    }

    @Override
    @Cacheable(cacheNames = "accountDetails", key = "#universalId", cacheManager = "accountDetailsCacheManager", unless = "#result == null || #result.getInternalClientNumber() == null")
    public AccountDetails createAccount(UUID universalId, CreateCsoAccountRequest createCsoAccountRequest) {
        return efilingAccountService.createAccount(createAccountRequestMapper.toCreateAccountRequest(universalId, createCsoAccountRequest));
    }

}
