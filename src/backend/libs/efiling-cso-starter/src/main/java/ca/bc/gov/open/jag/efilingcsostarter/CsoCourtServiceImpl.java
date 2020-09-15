package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.ceis.CsoAgencyArr;
import ca.bc.gov.ag.csows.ceis.CsoCourtClassArr;
import ca.bc.gov.ag.csows.ceis.CsoCourtLevelArr;
import ca.bc.gov.ag.csows.ceis.Csows;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.NestedEjbException;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacade;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


public class CsoCourtServiceImpl implements EfilingCourtService {

    private final Csows csows;
    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CsoCourtServiceImpl(Csows csows, FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
        this.csows = csows;
    }

    @Override
    public CourtDetails getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass) {
        if (StringUtils.isBlank(agencyIdentifierCd)) throw new IllegalArgumentException("Agency identifier is required");
        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("Level identifier is required");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("Class identifier is required");

        CsoAgencyArr csoAgencyArr = csows.getCourtLocations();

        return csoAgencyArr.getArray().stream()
                .filter(court -> court.getAgenAgencyIdentifierCd().equals(agencyIdentifierCd))
                .findFirst()
                .map(court -> new CourtDetails(court.getAgenId(), court.getAgenAgencyNm(), getClassDescription(courtClass), getCourLevelDescription(courtLevel)))
                .orElseThrow(() -> new EfilingCourtServiceException("Court not found"));
    }

    @Override
    public boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass) {
        try {
            // Check CSO for isParticipatingClass
            return filingStatusFacadeBean.isParticipatingClass(
                    agencyId,
                    courtLevel,
                    courtClass
            );
        } catch (ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception e) {
            throw new EfilingCourtServiceException("Exception while checking isParticipatingClass");
        }
    }

    private String getCourLevelDescription(String courtLevel) {
        CsoCourtLevelArr levels = csows.getCourtLevels();
        return levels.getArray().stream()
                .filter(court -> court.getLevelCd().equals(courtLevel))
                .findFirst()
                .map(court -> court.getLevelDsc())
                .orElseThrow(() -> new EfilingCourtServiceException("Level not found"));
    }

    private String getClassDescription(String courtClass) {
        CsoCourtClassArr classes = csows.getCourtClasses(null);
        return classes.getArray().stream()
                .filter(court -> court.getClassCd().equals(courtClass))
                .findFirst()
                .map(court -> court.getClassDsc())
                .orElseThrow(() -> new EfilingCourtServiceException("Class not found"));
    }

}
