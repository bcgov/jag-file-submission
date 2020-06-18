package ca.bc.gov.open.jag.efilingaccountclient;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DemoAccountServiceImpl implements EfilingAccountService {

    public CsoAccountDetails getAccountDetails(String userGuid) {

        // TODO - actual do some guid -> account details mapping
        List<String> roles = new ArrayList<>();
        roles.add("efiling");
        roles.add("access");
        CsoAccountDetails details = new CsoAccountDetails("AccountID 1", "ClientID 1", roles);

        return details;
    }
}
