package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.CourtsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@Service
public class CourtsApiDelegateImpl implements CourtsApiDelegate {

    Logger logger = LoggerFactory.getLogger(CourtsApiDelegateImpl.class);

    private final EfilingCourtLocationService efilingCourtLocationService;

    private final CourtLocationMapper courtLocationMapper;

    public CourtsApiDelegateImpl(EfilingCourtLocationService efilingCourtLocationService, CourtLocationMapper courtLocationMapper) {
        this.efilingCourtLocationService = efilingCourtLocationService;
        this.courtLocationMapper = courtLocationMapper;
    }

    @Override
    @RolesAllowed({"efiling-client", "efiling-admin"})
    public ResponseEntity<CourtLocations> getCourtLocations(String courtLevel) {

        logger.info("Request for court level received {}", courtLevel);
        List<CourtLocation> courtLocationsList = courtLocationMapper.toCourtLocationList(efilingCourtLocationService.getCourtLocations(courtLevel));
        if (courtLocationsList == null) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.COURT_LOCATION_ERROR).create(),
                HttpStatus.INTERNAL_SERVER_ERROR);

        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourts(courtLocationsList);
        return ResponseEntity.ok(courtLocations);

    }



}
