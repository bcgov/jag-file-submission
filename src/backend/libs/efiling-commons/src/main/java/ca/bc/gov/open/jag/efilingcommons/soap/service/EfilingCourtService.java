package ca.bc.gov.open.jag.efilingcommons.soap.service;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

public interface EfilingCourtService {
    CourtDetails getCourtDescription(String agencyIdentifierCd);
}
