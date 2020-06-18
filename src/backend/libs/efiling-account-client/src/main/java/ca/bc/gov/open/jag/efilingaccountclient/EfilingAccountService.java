package ca.bc.gov.open.jag.efilingaccountclient;

/**
 * Interface for getting CSO account details based on a user GUID
 */
public interface EfilingAccountService {

    CsoAccountDetails getAccountDetails(String userGuid);
}
