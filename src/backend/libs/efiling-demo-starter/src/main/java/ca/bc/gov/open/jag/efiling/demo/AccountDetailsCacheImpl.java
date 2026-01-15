package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class AccountDetailsCacheImpl implements AccountDetailsCache {

    @Override
    @CachePut(cacheNames = "account", key = "#accountDetails.universalId", cacheManager = "demoAccountCacheManager", unless="#result == null")
    public AccountDetails put(AccountDetails accountDetails) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = "account", key = "#userGuid", cacheManager = "demoAccountCacheManager", unless="#result == null")
    public AccountDetails get(String userGuid) {
        return null;
    }

}
