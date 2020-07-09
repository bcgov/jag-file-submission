package ca.bc.gov.open.jag.efiling.demo;


import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class EfilingAccountServiceDemoImpl implements EfilingAccountService {

    @Cacheable(cacheNames = "account", key = "#userGuid", unless="#result == null", cacheManager = "demoAccountCacheManager")
    public AccountDetails getAccountDetails(UUID userGuid, String bceidAccountType) {
        return null;
    }

    @CachePut(cacheNames = "account", key = "#createAccountRequest.universalId", cacheManager = "demoAccountCacheManager")
    public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {

        AccountDetails accountDetails = new AccountDetails.Builder()
                .universalId(createAccountRequest.getUniversalId())
                .accountId(BigDecimal.ONE)
                .clientId(BigDecimal.ONE)
                .email(createAccountRequest.getEmail())
                .firstName(createAccountRequest.getFirstName())
                .lastName(createAccountRequest.getLastName())
                .middleName(createAccountRequest.getMiddleName())
                .create();

        return accountDetails;

    }

}
