package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {

    AccountDetails getCsoAccountDetails(UUID universalId);

    void updateClient(String internalClientNumber);

}
