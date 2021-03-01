package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

import java.math.BigDecimal;
import java.util.Optional;

public interface EfilingCourtService {

    Optional<CourtDetails> getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass);

    boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode);

    boolean checkValidCourtFileNumber(String fileNumber, BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode);

}
