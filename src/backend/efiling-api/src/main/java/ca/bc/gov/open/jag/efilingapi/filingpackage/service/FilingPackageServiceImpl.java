package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class FilingPackageServiceImpl implements FilingPackageService {

    private final EfilingStatusService efilingStatusService;

    private final AccountService accountService;

    public FilingPackageServiceImpl(EfilingStatusService efilingStatusService, AccountService accountService) {
        this.efilingStatusService = efilingStatusService;
        this.accountService = accountService;
    }


    @Override
    public Optional<FilingPackage> getCSOFilingPackage(UUID universalId, BigDecimal packageNumber) {
        return Optional.empty();
    }
}
