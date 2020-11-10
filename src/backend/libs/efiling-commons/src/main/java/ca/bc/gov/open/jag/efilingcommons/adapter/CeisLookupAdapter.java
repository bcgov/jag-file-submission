package ca.bc.gov.open.jag.efilingcommons.adapter;

import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;

import java.util.List;

public interface CeisLookupAdapter {
    List<InternalCourtLocation> getCourLocations(String courtType);
}
