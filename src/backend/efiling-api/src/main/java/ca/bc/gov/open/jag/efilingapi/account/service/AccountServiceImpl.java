package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private static final String BCEID_ACCOUNT_TYPE = "Individual";
    private final EfilingAccountService efilingAccountService;

    public AccountServiceImpl(EfilingAccountService efilingAccountService) {
        this.efilingAccountService = efilingAccountService;
    }

    @Override
    public AccountDetails getCsoAccountDetails(UUID universalId) {
        return efilingAccountService.getAccountDetails(universalId, BCEID_ACCOUNT_TYPE);
    }

}
