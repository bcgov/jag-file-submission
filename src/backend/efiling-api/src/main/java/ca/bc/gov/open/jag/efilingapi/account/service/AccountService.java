package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import java.util.UUID;

public interface AccountService {

    AccountDetails getCsoAccountDetails(UUID universalId);

    void updateClient(AccountDetails accountDetails);

    AccountDetails createAccount(UUID universalId, CreateCsoAccountRequest createAccountRequest);

}
