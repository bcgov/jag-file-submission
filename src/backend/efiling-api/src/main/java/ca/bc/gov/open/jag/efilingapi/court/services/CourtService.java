package ca.bc.gov.open.jag.efilingapi.court.services;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequestRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

public interface CourtService {

    Boolean isValidCourtLocation(IsValidCourtRequest isValidCourtRequest);

    CourtDetails getCourtDetails(GetCourtDetailsRequest getCourtDetailsRequest);

    boolean IsValidCourt(IsValidCourtRequestRequest isValidCourtRequestRequest);

}
