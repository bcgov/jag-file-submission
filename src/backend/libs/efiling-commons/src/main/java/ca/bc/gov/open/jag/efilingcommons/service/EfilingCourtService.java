package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

import java.math.BigDecimal;

public interface EfilingCourtService {
    CourtDetails getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass);

    boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode);

    boolean checkValidCourtFileNumber(String fileNumber, BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode);
}
