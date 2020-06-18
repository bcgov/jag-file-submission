package ca.bc.gov.open.jag.efilingaccountclient;


import org.springframework.stereotype.Service;

@Service
public class DemoAccountServiceImpl implements EfilingAccountService{

    public CsoAccountDetails getAccountDetails(String userGuid) {

        // TODO - actual do some guid -> account details mapping
        String[] roles = {"efiling", "access"};
        CsoAccountDetails details = new CsoAccountDetails("AccountID 1", "ClientID 1", roles);

        return details;
    }
}
