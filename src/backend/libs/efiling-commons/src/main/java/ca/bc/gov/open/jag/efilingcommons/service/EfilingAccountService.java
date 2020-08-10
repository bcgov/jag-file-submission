package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;

import java.util.UUID;

/**
 * Interface for getting account details based on a user GUID
 */
public interface EfilingAccountService {

    /**
     * Get the account details
     * @param universalId
     * @return
     */
    AccountDetails getAccountDetails(UUID universalId);

    /**
     * Creates a new account
     * @param createAccountRequest
     * @return
     */
    AccountDetails createAccount(CreateAccountRequest createAccountRequest);

}
