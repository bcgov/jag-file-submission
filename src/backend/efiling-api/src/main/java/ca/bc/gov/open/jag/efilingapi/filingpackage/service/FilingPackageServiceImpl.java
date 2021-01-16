package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class FilingPackageServiceImpl implements FilingPackageService {

    private final EfilingReviewService efilingReviewService;

    private final AccountService accountService;

    private final FilingPackageMapper filingPackageMapper;

    public FilingPackageServiceImpl(EfilingReviewService efilingReviewService, AccountService accountService, FilingPackageMapper filingPackageMapper) {
        this.efilingReviewService = efilingReviewService;
        this.accountService = accountService;
        this.filingPackageMapper = filingPackageMapper;
    }


    @Override
    public Optional<FilingPackage> getCSOFilingPackage(UUID universalId, BigDecimal packageNumber) {
        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) return Optional.empty();

        FilingPackageRequest request = new FilingPackageRequest(accountDetails.getClientId(), packageNumber);

        Optional<ReviewFilingPackage> filingPackage = efilingReviewService.findStatusByPackage(request);

        if (!filingPackage.isPresent()) return Optional.empty();

        return filingPackage.map(filingPackageMapper::toResponseFilingPackage);

    }
}
