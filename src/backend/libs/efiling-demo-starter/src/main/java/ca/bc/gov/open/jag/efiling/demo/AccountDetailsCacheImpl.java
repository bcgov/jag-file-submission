package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class AccountDetailsCacheImpl implements AccountDetailsCache {

    @Override
    @CachePut(cacheNames = "account", key = "#accountDetails.universalId", cacheManager = "demoAccountCacheManager")
    public AccountDetails put(AccountDetails accountDetails) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = "account", key = "#userGuid", unless="#result == null", cacheManager = "demoAccountCacheManager")
    public AccountDetails get(UUID userGuid) {
        return null;
    }

}
