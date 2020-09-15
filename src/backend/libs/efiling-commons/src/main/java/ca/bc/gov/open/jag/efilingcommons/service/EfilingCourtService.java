package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

public interface EfilingCourtService {
    CourtDetails getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass);

    boolean checkValidLevelClassLocation(String courtLevel, String courtClass);
}
