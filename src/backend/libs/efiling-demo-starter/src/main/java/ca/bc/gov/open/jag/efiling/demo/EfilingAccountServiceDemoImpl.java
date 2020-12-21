package ca.bc.gov.open.jag.efiling.demo;


import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.UUID;

public class EfilingAccountServiceDemoImpl implements EfilingAccountService {

    @Cacheable(cacheNames = "account", key = "#userGuid", unless="#result == null", cacheManager = "demoAccountCacheManager")
    public AccountDetails getAccountDetails(UUID userGuid) {
        AccountDetails accountDetails = AccountDetails.builder().fileRolePresent(true).create();

        return accountDetails;
    }

    @CachePut(cacheNames = "account", key = "#createAccountRequest.universalId", cacheManager = "demoAccountCacheManager")
    public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {

        AccountDetails accountDetails = new AccountDetails.Builder()
                .universalId(createAccountRequest.getUniversalId())
                .accountId(BigDecimal.valueOf(437))
                .clientId(BigDecimal.valueOf(752))
                .fileRolePresent(createAccountRequest.getUniversalId() != Keys.ACCOUNT_WITHOUT_EFILING_ROLE)
                .cardRegistered(true)
                .create();

        return accountDetails;

    }

    @Override
    public void updateClient(AccountDetails accountDetails) {
        //Void not doing a thing in demo mode
    }

    @Override
    public String getOrderNumber() {
        return "1234";
    }

}
