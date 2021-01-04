package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import java.util.UUID;

public interface AccountDetailsCache {


    AccountDetails put(AccountDetails accountDetails);

    AccountDetails get(UUID userGuid);

}
