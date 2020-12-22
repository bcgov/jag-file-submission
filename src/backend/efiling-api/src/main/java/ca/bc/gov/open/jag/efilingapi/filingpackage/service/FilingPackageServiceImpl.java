package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class FilingPackageServiceImpl implements FilingPackageService {

    private final EfilingStatusService efilingStatusService;

    private final AccountService accountService;

    private final FilingPackageMapper filingPackageMapper;

    public FilingPackageServiceImpl(EfilingStatusService efilingStatusService, AccountService accountService, FilingPackageMapper filingPackageMapper) {
        this.efilingStatusService = efilingStatusService;
        this.accountService = accountService;
        this.filingPackageMapper = filingPackageMapper;
    }


    @Override
    public Optional<FilingPackage> getCSOFilingPackage(UUID universalId, BigDecimal packageNumber) {
        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) return Optional.empty();

        FilingPackageRequest request = new FilingPackageRequest(accountDetails.getClientId(), packageNumber);

        Optional<ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage> filingPackage = efilingStatusService.findStatusByPackage(request);

        return filingPackage.map(filingPackageMapper::toResponseFilingPackage);

    }
}
