package ca.bc.gov.open.jag.efilingaccountclient;


import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DemoAccountServiceImpl implements EfilingAccountService {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITHOUT_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");

    private Map<String, AccountDetails> csoAccounts = new HashMap<>();

    public DemoAccountServiceImpl() {

        AccountDetails accountWithEfilingRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "", "", "", "");
        AccountDetails accountWithoutEfilingRole  = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, false, "", "", "", "");

        csoAccounts.put(ACCOUNT_WITH_EFILING_ROLE.toString(), accountWithEfilingRole);
        csoAccounts.put(ACCOUNT_WITHOUT_EFILING_ROLE.toString(), accountWithoutEfilingRole );

    }


    public AccountDetails getAccountDetails(String userGuid) {
        return csoAccounts.get(userGuid);
    }
}
