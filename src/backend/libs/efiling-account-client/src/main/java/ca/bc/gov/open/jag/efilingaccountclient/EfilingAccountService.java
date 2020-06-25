package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;

/**
 * Interface for getting CSO account details based on a user GUID
 */
public interface EfilingAccountService {

    CsoAccountDetails getAccountDetails(String userGuid) throws NestedEjbException_Exception;
}
