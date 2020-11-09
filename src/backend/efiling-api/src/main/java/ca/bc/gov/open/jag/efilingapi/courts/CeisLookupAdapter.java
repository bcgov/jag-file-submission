package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CeisLookupAdapter {

    private final DefaultApi defaultApi;

    private final CourtLocationMapper courtLocationMapper;

    public CeisLookupAdapter(DefaultApi defaultApi, CourtLocationMapper courtLocationMapper) {
        this.defaultApi = defaultApi;
        this.courtLocationMapper = courtLocationMapper;
    }

    public CourtLocations getCourLocations(String courtType) throws ApiException {

        List<CourtLocation> courtLocationList = ((List<ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation>) defaultApi.courtLocationsGet()).stream()
                .filter(courtLocation -> isSearchedType(courtLocation.getIssupremecourt(), courtLocation.getIsprovincialcourt(), courtType))
                .map(courtLocationMapper::toCourtLocation)
                .collect(Collectors.toList());;
        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourts(courtLocationList);
        return courtLocations;

    }

    private boolean isSearchedType(String isSupreme, String isProvincial, String courtType) {
        if (StringUtils.isEmpty(courtType)) return true;
        if (courtType.equals("P") && (!StringUtils.isEmpty(isProvincial)) && isProvincial.equals("Y")) return true;
        if (courtType.equals("S") && (!StringUtils.isEmpty(isSupreme)) && isSupreme.equals("Y")) return true;

        return false;
    }
}
