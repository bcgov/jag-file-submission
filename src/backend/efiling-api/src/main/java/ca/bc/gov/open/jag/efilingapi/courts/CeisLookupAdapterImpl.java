package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingcommons.adapter.CeisLookupAdapter;
import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CeisLookupAdapterImpl implements CeisLookupAdapter {

    Logger logger = LoggerFactory.getLogger(CeisLookupAdapterImpl.class);

    private final DefaultApi defaultApi;

    private final CourtLocationMapper courtLocationMapper;

    public CeisLookupAdapterImpl(DefaultApi defaultApi, CourtLocationMapper courtLocationMapper) {
        this.defaultApi = defaultApi;
        this.courtLocationMapper = courtLocationMapper;
    }

    @Override
    public List<InternalCourtLocation> getCourLocations(String courtType) {
        try {
            List<InternalCourtLocation> courtLocationList = defaultApi.courtLocationsGet().getCourtlocations().stream()
                    .filter(courtLocation -> isSearchedType(courtLocation.getIssupremecourt(), courtLocation.getIsprovincialcourt(), courtType))
                    .map(courtLocationMapper::toCourtLocation)
                    .collect(Collectors.toList());

            return courtLocationList;
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    private boolean isSearchedType(String isSupreme, String isProvincial, String courtType) {
        if (StringUtils.isEmpty(courtType)) return true;
        if (courtType.equals("S") && (!StringUtils.isEmpty(isSupreme)) && isSupreme.equals("Y")) return true;
        if (courtType.equals("P") && (!StringUtils.isEmpty(isProvincial)) && isProvincial.equals("Y")) return true;

        return false;
    }
}
