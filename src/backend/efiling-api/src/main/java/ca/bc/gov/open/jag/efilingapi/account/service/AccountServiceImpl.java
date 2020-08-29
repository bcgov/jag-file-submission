package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final EfilingAccountService efilingAccountService;

    public AccountServiceImpl(EfilingAccountService efilingAccountService) {
        this.efilingAccountService = efilingAccountService;
    }

    @Override
    @Cacheable(cacheNames = "accountDetails", key = "#universalId", cacheManager = "accountDetailsCacheManager", unless = "#result == null || result.internalClientNumber == null")
    public AccountDetails getCsoAccountDetails(UUID universalId) {
        return efilingAccountService.getAccountDetails(universalId);
    }

    @Override
    public void updateClient(AccountDetails accountDetails) {
        efilingAccountService.updateClient(accountDetails);
    }

}
