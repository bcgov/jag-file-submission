package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;

import java.math.BigDecimal;

public class EfilingCourtServiceFake implements EfilingCourtService {

    @Override
    public CourtDetails getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass) {
        return null;
    }

    @Override
    public boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode) {
        return false;
    }

    @Override
    public boolean checkValidCourtFileNumber(String fileNumber, BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode) {
        return false;
    }
}
