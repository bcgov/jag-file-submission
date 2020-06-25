package ca.bc.gov.open.jag.efilingaccountclient;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DemoAccountServiceImpl implements EfilingAccountService {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITHOUT_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");

    private Map<String, CsoAccountDetails> csoAccounts = new HashMap<>();

    public DemoAccountServiceImpl() {

        CsoAccountDetails accountWithEfilingRole = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, true);
        CsoAccountDetails accountWithOutEfilingRole = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, false);

        csoAccounts.put(ACCOUNT_WITH_EFILING_ROLE.toString(), accountWithEfilingRole);
        csoAccounts.put(ACCOUNT_WITHOUT_EFILING_ROLE.toString(), accountWithOutEfilingRole);

    }


    public CsoAccountDetails getAccountDetails(String userGuid) {
        return csoAccounts.get(userGuid);
    }
}
