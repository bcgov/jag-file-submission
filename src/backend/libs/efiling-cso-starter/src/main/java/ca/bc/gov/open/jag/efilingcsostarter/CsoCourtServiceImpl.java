package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.ceis.CsoAgencyArr;
import ca.bc.gov.ag.csows.ceis.CsoCourtClassArr;
import ca.bc.gov.ag.csows.ceis.CsoCourtLevelArr;
import ca.bc.gov.ag.csows.ceis.Csows;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import org.apache.commons.lang3.StringUtils;


public class CsoCourtServiceImpl implements EfilingCourtService {

    private final Csows csows;

    public CsoCourtServiceImpl(Csows csows) {
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
    public boolean checkValidLevelClassLocation(String courtLevel, String courtClass) {
        return false;
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
