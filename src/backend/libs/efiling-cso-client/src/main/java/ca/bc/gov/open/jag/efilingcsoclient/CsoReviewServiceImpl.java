package ca.bc.gov.open.jag.efilingcsoclient;


import ca.bc.gov.ag.csows.filing.status.FilingStatus;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.reports.Report;
import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsoReviewServiceImpl implements EfilingReviewService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    private final ReportService reportService;

    private final FilePackageMapper filePackageMapper;

    public CsoReviewServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean, ReportService reportService, FilePackageMapper filePackageMapper) {

        this.filingStatusFacadeBean = filingStatusFacadeBean;
        this.reportService = reportService;
        this.filePackageMapper = filePackageMapper;

    }

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {

        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id and package service ");

            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, filingPackageRequest.getPackageNo(), filingPackageRequest.getClientId(), null, null, null, null, BigDecimal.ONE, null);

            if (filingStatus.getFilePackages() == null || filingStatus.getFilePackages().isEmpty()) return Optional.empty();

            return Optional.of(filePackageMapper.toFilingPackage(filingStatus.getFilePackages().get(0)));

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status", e.getCause());

        }

    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id service ");

            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, null, filingPackageRequest.getClientId(), null, null, null, null, BigDecimal.valueOf(100), null);

            if (filingStatus.getFilePackages().isEmpty()) return new ArrayList<>();

            return filingStatus.getFilePackages().stream().map(filePackageMapper::toFilingPackage).collect(Collectors.toList());

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status list", e.getCause());

        }
    }

    @Override
    public Optional<byte[]> getSubmissionSheet(BigDecimal packageNumber) {
        Report report = new Report();
        report.setName(Keys.REPORT_NAME);
        report.getParameters().addAll(Arrays.asList(Keys.REPORT_PARAMETER, packageNumber.toPlainString()));

        byte[] result = reportService.runReport(report);

        if (result == null || result.length == 0) return Optional.empty();

        return Optional.of(result);
    }
}
