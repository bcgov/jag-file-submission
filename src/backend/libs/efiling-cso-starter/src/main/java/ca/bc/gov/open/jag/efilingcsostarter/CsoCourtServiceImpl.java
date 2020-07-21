package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.ceis.CsoAgencyArr;
import ca.bc.gov.ag.csows.ceis.CsoAgencyRec;
import ca.bc.gov.ag.csows.ceis.Csows;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class CsoCourtServiceImpl implements EfilingCourtService {

    private Csows csows;

    public CsoCourtServiceImpl(Csows csows) {
        this.csows = csows;
    }

    @Override
    public String getCourtDescription(String agencyIdentifierCd) {
        if (StringUtils.isBlank(agencyIdentifierCd)) throw new IllegalArgumentException("Agency identifier is required");

        CsoAgencyArr csoAgencyArr = csows.getCourtLocations();
        Optional<CsoAgencyRec> csoAgencyRec =  csoAgencyArr.getArray().stream()
                .filter(court -> court.getAgenAgencyIdentifierCd().equals(agencyIdentifierCd))
                .findFirst();
        if (csoAgencyRec.isPresent()) {
            return csoAgencyRec.get().getAgenAgencyNm();
        } else {
            throw new EfilingCourtServiceException("Court not found");
        }
    }
}
