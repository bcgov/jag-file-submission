package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.CourtsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CourtsApiDelegateImpl implements CourtsApiDelegate {

    Logger logger = LoggerFactory.getLogger(CourtsApiDelegateImpl.class);

    private final CeisLookupAdapter ceisLookupAdapter;

    public CourtsApiDelegateImpl(CeisLookupAdapter ceisLookupAdapter) {
        this.ceisLookupAdapter = ceisLookupAdapter;
    }

    @Override
    public ResponseEntity<CourtLocations> getCourtLocations(String courtLevel) {
        try {
            logger.info("Request for court level recieved {}", courtLevel);
            return ResponseEntity.ok(ceisLookupAdapter.getCourLocations(courtLevel));
        } catch (ApiException e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.COURT_LOCATION_ERROR).create(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
