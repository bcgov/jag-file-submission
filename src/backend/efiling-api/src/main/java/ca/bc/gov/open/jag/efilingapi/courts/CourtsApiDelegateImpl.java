package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.CourtsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.Address;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

public class CourtsApiDelegateImpl implements CourtsApiDelegate {
    @Override
    public ResponseEntity<CourtLocations> getCourtLocations(String courtLevel) {
        CourtLocation courtLocation = new CourtLocation();
        courtLocation.setId(BigDecimal.ONE);
        courtLocation.setName("Test");
        courtLocation.setCode("Test");
        courtLocation.setIsSupremeCourt(true);
        Address address = new Address();
        address.setAddressLine1("Test");
        address.setAddressLine2("Test");
        address.setAddressLine3("Test");
        address.setPostalCode("Test");
        address.setCityName("Test");
        address.setProvinceName("Test");
        address.setCountryName("Test");
        courtLocation.setAddress(address);
        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourts(Arrays.asList(courtLocation));
        return ResponseEntity.ok(courtLocations);
    }

}
