package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.CourtsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.Address;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class CourtsApiDelegateImpl implements CourtsApiDelegate {
    @Override
    public ResponseEntity<CourtLocations> getCourtLocations(String courtLevel) {
        return ResponseEntity.ok(buildMockData());
    }

    private CourtLocations buildMockData() {
        CourtLocation courtLocationOne = new CourtLocation();
        courtLocationOne.setId(BigDecimal.valueOf(1031));
        courtLocationOne.setName("Campbell River");
        courtLocationOne.setCode("MockCode");
        courtLocationOne.setIsSupremeCourt(true);
        Address addressOne = new Address();
        addressOne.setAddressLine1("500 - 13th Avenue");
        addressOne.setAddressLine2("");
        addressOne.setAddressLine3("");
        addressOne.setPostalCode("V9W 6P1");
        addressOne.setCityName("Campbell River");
        addressOne.setProvinceName("British Columbia");
        addressOne.setCountryName("Canada");
        courtLocationOne.setAddress(addressOne);

        CourtLocation courtLocationTwo = new CourtLocation();
        courtLocationTwo.setId(BigDecimal.valueOf(3521));
        courtLocationTwo.setName("Chilliwack");
        courtLocationTwo.setCode("MockCode");
        courtLocationTwo.setIsSupremeCourt(true);
        Address addressTwo = new Address();
        addressTwo.setAddressLine1("46085 Yale Road");
        addressTwo.setAddressLine2("");
        addressTwo.setAddressLine3("");
        addressTwo.setPostalCode("V2P 2L8");
        addressTwo.setCityName("Chilliwack");
        addressTwo.setProvinceName("British Columbia");
        addressTwo.setCountryName("Canada");
        courtLocationTwo.setAddress(addressTwo);

        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourts(Arrays.asList(courtLocationOne,courtLocationTwo));
        return courtLocations;
    }

}
