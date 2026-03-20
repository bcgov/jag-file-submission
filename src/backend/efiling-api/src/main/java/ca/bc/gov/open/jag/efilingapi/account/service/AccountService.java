package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

public interface AccountService {

    AccountDetails getCsoAccountDetails(String universalId);

    void updateClient(AccountDetails accountDetails);

    AccountDetails createAccount(String universalId, String identityProvider,CreateCsoAccountRequest createAccountRequest);

}
