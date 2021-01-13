package ca.bc.gov.open.jag.efilingcsoclient;


import ca.bc.gov.ag.csows.filing.status.FilingStatus;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CsoStatusServiceImpl implements EfilingStatusService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    private final FilePackageMapper filePackageMapper;

    public CsoStatusServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean, FilePackageMapper filePackageMapper) {

        this.filingStatusFacadeBean = filingStatusFacadeBean;

        this.filePackageMapper = filePackageMapper;
    }

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {

        try {

            logger.info("Calling soap service");

            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, filingPackageRequest.getPackageNo(), filingPackageRequest.getClientId(), null, null, null, null, BigDecimal.ONE, null);

            if (filingStatus.getFilePackages().isEmpty()) return Optional.empty();

            return Optional.of(filePackageMapper.toFilingPackage(filingStatus.getFilePackages().get(0)));

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status", e.getCause());

        }

    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
        return null;
    }
}
