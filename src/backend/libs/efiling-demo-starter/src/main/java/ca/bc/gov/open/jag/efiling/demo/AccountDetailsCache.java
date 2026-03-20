package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

public interface AccountDetailsCache {


    AccountDetails put(AccountDetails accountDetails);

    AccountDetails get(String userGuid);

}
