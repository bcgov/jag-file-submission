package ca.bc.gov.open.jag.efilingcommons.court;

import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;

import java.util.List;

public interface CourtLocationService {


    List<InternalCourtLocation> getCourtLocations(String courtType);


}
