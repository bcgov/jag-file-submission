package ca.bc.gov.open.jag.efilingaccountclient;

public interface EfilingAccountService {

    CsoAccountDetails getAccountDetails(String userGuid);
}
