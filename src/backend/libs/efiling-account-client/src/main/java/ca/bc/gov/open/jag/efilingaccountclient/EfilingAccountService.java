package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

/**
 * Interface for getting CSO account details based on a user GUID
 */
public interface EfilingAccountService {

    AccountDetails getAccountDetails(String userGuid) throws NestedEjbException_Exception;
}
