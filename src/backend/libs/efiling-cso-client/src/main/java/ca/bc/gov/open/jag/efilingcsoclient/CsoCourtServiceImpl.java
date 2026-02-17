package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.ceis.*;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;


public class CsoCourtServiceImpl implements EfilingCourtService {

    private final Csows csows;
    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CsoCourtServiceImpl(Csows csows, FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
        this.csows = csows;
    }

    @Override
    public Optional<CourtDetails> getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass) {

        if (StringUtils.isBlank(agencyIdentifierCd)) throw new IllegalArgumentException("Agency identifier is required");
        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("Level identifier is required");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("Class identifier is required");

        return csows.getCourtLocations().getArray().stream()
                .filter(court -> court.getAgenAgencyIdentifierCd().equals(agencyIdentifierCd))
                .findFirst()
                .map(court -> new CourtDetails(court.getAgenId(), court.getAgenAgencyNm(), getClassDescription(courtClass), getCourLevelDescription(courtLevel)));
    }

    @Override
    public boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass, String appplicationCode) {
        try {
            // Call CSO for isParticipatingClass
            return filingStatusFacadeBean.isParticipatingClass(
                    agencyId,
                    courtLevel,
                    courtClass,
                    appplicationCode
            );
        } catch (ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception e) {
            throw new EfilingCourtServiceException("Exception while checking isParticipatingClass");
        }
    }

    @Override
    public boolean checkValidCourtFileNumber(String fileNumber, BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode) {
        // Call CSO for fileNumberSearch
        CsoFileSrchResultRet fileNumberSearchResult = csows.fileNumberSearch(
                BigDecimal.ZERO,
                fileNumber,
                agencyId,
                courtLevel,
                courtClass,
                null,
                null,
                BigDecimal.ONE,
                Boolean.TRUE,
                applicationCode
        );

        // check if total records is greater than zero
        return (fileNumberSearchResult.getTotalRecords().compareTo(BigDecimal.ZERO) == 1);
    }

    private String getCourLevelDescription(String courtLevel) {
        CsoCourtLevelArr levels = csows.getCourtLevels();
        return levels.getArray().stream()
                .filter(court -> court.getLevelCd().equals(courtLevel))
                .findFirst()
                .map(CsoCourtLevelRec::getLevelDsc)
                .orElseThrow(() -> new EfilingCourtServiceException("Level not found"));
    }

    private String getClassDescription(String courtClass) {
        CsoCourtClassArr classes = csows.getCourtClasses(null);
        return classes.getArray().stream()
                .filter(court -> court.getClassCd().equals(courtClass))
                .findFirst()
                .map(CsoCourtClassRec::getClassDsc)
                .orElseThrow(() -> new EfilingCourtServiceException("Class not found"));
    }

}
