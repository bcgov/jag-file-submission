package ca.bc.gov.open.jag.efilingapi.court.services;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtFileNumberRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;

public interface CourtService {

    boolean isValidCourt(IsValidCourtRequest isValidCourtRequest);

    CourtDetails getCourtDetails(GetCourtDetailsRequest getCourtDetailsRequest);

    boolean isValidCourtFileNumber(IsValidCourtFileNumberRequest isValidCourtFileNumberRequest);
}
