package preview.ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import preview.ca.bc.gov.ag.csows.filing.status.FilingStatus;
import preview.ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import preview.ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import preview.ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PreviewCsoReviewServiceImpl extends CsoReviewServiceImpl implements EfilingReviewService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FilingStatusFacadeBean previewFilingStatusFacadeBean;

    private final FilePackageMapper previewFilePackageMapper;

    private final CsoProperties csoProperties;

    public PreviewCsoReviewServiceImpl(ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean filingStatusFacadeBean, ReportService reportService, FilingFacadeBean filingFacadeBean, ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper filePackageMapper, CsoProperties csoProperties, RestTemplate restTemplate, FilingStatusFacadeBean previewFilingStatusFacadeBean, FilePackageMapper previewFilePackageMapper) {
        super(filingStatusFacadeBean, reportService, filingFacadeBean, filePackageMapper, csoProperties, restTemplate);
        this.previewFilingStatusFacadeBean = previewFilingStatusFacadeBean;
        this.previewFilePackageMapper = previewFilePackageMapper;
        this.csoProperties = csoProperties;
    }

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {

        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id and package service ");

            FilingStatus filingStatus = previewFilingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, filingPackageRequest.getPackageNo(), filingPackageRequest.getClientId(), null, null, null, null, BigDecimal.ONE, null);

            if (filingStatus.getFilePackages() == null || filingStatus.getFilePackages().isEmpty()) return Optional.empty();

            return Optional.of(previewFilePackageMapper.toFilingPackage(
                    filingStatus.getFilePackages().get(0),
                    getViewAllPackagesUrl(),
                    filingStatus.getFilePackages().get(0).getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.INDIVIDUAL_ROLE_TYPE_CD))
                            .map(previewFilePackageMapper::toParty)
                            .collect(Collectors.toList()),
                    filingStatus.getFilePackages().get(0).getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.ORGANIZATION_ROLE_TYPE_CD))
                            .map(previewFilePackageMapper::toOrganization)
                            .collect(Collectors.toList())));

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status", e.getCause());

        }

    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id service ");

            FilingStatus filingStatus = previewFilingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, null, filingPackageRequest.getClientId(), null, null, null, null, BigDecimal.valueOf(100), null);

            if (filingStatus.getFilePackages().isEmpty()) return new ArrayList<>();

            return filingStatus.getFilePackages().stream().map(filePackage -> previewFilePackageMapper.toFilingPackage(filePackage,
                    getViewAllPackagesUrl(),
                    filePackage.getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.INDIVIDUAL_ROLE_TYPE_CD))
                            .map(previewFilePackageMapper::toParty)
                            .collect(Collectors.toList()),
                    filePackage.getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.ORGANIZATION_ROLE_TYPE_CD))
                            .map(previewFilePackageMapper::toOrganization)
                            .collect(Collectors.toList())
                    )).collect(Collectors.toList());

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status list", e.getCause());

        }
    }

    private String getViewAllPackagesUrl() {
        return MessageFormat.format("{0}/{1}", csoProperties.getCsoBasePath(),  Keys.VIEW_ALL_PACKAGE_SUBPATH);
    }

}
