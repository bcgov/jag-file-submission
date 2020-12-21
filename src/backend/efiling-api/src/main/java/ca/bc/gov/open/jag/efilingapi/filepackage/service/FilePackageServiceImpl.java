package ca.bc.gov.open.jag.efilingapi.filepackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;

import java.math.BigDecimal;
import java.util.UUID;

public class FilePackageServiceImpl implements FilePackageService {

    private final EfilingStatusService efilingStatusService;

    private final AccountService accountService;

    public FilePackageServiceImpl(EfilingStatusService efilingStatusService, AccountService accountService) {
        this.efilingStatusService = efilingStatusService;
        this.accountService = accountService;
    }


    @Override
    public FilingPackage getCSOFilingPackage(UUID universalId, BigDecimal packageNumber) {
        return null;
    }
}
