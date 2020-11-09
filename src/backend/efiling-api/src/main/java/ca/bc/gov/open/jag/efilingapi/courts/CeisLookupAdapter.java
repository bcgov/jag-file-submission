package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation;

import java.util.List;

public class CeisLookupAdapter {

    private final DefaultApi defaultApi;

    public CeisLookupAdapter(DefaultApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    public CourtLocations getCourLocations(String courtType) throws ApiException {
        List<CourtLocation> courtLocations = (List<CourtLocation>) defaultApi.courtLocationsGet();
        return null;
    }
}
