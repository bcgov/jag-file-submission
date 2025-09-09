package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.CourtsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingapi.error.CourtLocationException;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourtsApiDelegateImpl implements CourtsApiDelegate {

    private static final String COURT_LOCATION_ERROR = "Error while retrieving court locations.";

    Logger logger = LoggerFactory.getLogger(CourtsApiDelegateImpl.class);

    private final EfilingCourtLocationService efilingCourtLocationService;

    private final CourtLocationMapper courtLocationMapper;

    public CourtsApiDelegateImpl(EfilingCourtLocationService efilingCourtLocationService, CourtLocationMapper courtLocationMapper) {
        this.efilingCourtLocationService = efilingCourtLocationService;
        this.courtLocationMapper = courtLocationMapper;
    }

    @Override
    @PreAuthorize("hasRole('efiling-client') || hasRole('efiling-admin')")
    public ResponseEntity<CourtLocations> getCourtLocations(String courtLevel) {

        logger.info("Request for court level received {}", courtLevel);
        List<CourtLocation> courtLocationsList = courtLocationMapper.toCourtLocationList(efilingCourtLocationService.getCourtLocations(courtLevel));
        if (courtLocationsList == null)
            throw new CourtLocationException(COURT_LOCATION_ERROR);

        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourts(courtLocationsList);
        return ResponseEntity.ok(courtLocations);

    }



}
