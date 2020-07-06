package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import java.util.UUID;

/**
 * Interface for getting CSO account details based on a user GUID
 */
public interface EfilingAccountService {

    AccountDetails getAccountDetails(UUID userGuid, String bceidAccountType);

}
