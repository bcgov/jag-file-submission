package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

/**
 * Interface for getting CSO account details based on a user GUID
 */
public interface EfilingAccountService {

    AccountDetails getAccountDetails(String userGuid, String bceidAccountType);
}
