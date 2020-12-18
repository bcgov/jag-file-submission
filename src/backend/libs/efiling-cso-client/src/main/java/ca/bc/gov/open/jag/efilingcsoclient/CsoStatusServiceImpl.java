package ca.bc.gov.open.jag.efilingcsoclient;


import ca.bc.gov.ag.csows.filing.status.FilingStatus;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.FilePackage;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;

import java.math.BigDecimal;
import java.util.Optional;

public class CsoStatusServiceImpl implements EfilingStatusService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    private final FilePackageMapper filePackageMapper;

    public CsoStatusServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean, FilePackageMapper filePackageMapper) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
        this.filePackageMapper = filePackageMapper;
    }

    @Override
    public Optional<FilePackage> findStatusByPackage(BigDecimal clientId, BigDecimal packageNo) {
        try {

            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null,null,null,null, null,null,packageNo, clientId,null,null,null,null,BigDecimal.ONE,null);

            if (filingStatus.getFilePackages().isEmpty()) return Optional.empty();

            return  Optional.of(filePackageMapper.toFilePackage(filingStatus.getFilePackages().get(0)));

        } catch (NestedEjbException_Exception e) {
            throw new EfilingStatusServiceException("Exception while finding status", e.getCause());
        }
    }
}
