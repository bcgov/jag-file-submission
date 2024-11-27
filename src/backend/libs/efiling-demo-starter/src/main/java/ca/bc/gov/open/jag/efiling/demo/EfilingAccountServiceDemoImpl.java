package ca.bc.gov.open.jag.efiling.demo;


import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;

public class EfilingAccountServiceDemoImpl implements EfilingAccountService {

    private final AccountDetailsCache accountDetailsCache;

    public EfilingAccountServiceDemoImpl(AccountDetailsCache accountDetailsCache) {
        this.accountDetailsCache = accountDetailsCache;
    }

    @Cacheable(cacheNames = "account", key = "#userGuid", unless="#result == null", cacheManager = "demoAccountCacheManager")
    public AccountDetails getAccountDetails(String userGuid) {

        return accountDetailsCache.get(userGuid);

    }

    @CachePut(cacheNames = "account", key = "#p0.getUniversalId()", unless = "#createAccountRequest == null", cacheManager = "demoAccountCacheManager")
    public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {

        AccountDetails accountDetails = new AccountDetails.Builder()
                .universalId(createAccountRequest.getUniversalId())
                .accountId(BigDecimal.valueOf(437))
                .clientId(BigDecimal.valueOf(752))
                .fileRolePresent(createAccountRequest.getUniversalId() != Keys.ACCOUNT_WITHOUT_EFILING_ROLE)
                .cardRegistered(true)
                .create();

        this.accountDetailsCache.put(accountDetails);

        return accountDetails;

    }

    @Override
    public void updateClient(AccountDetails accountDetails) {
        this.accountDetailsCache.put(accountDetails);
    }

    @Override
    public String getOrderNumber() {
        return "1234";
    }

}
