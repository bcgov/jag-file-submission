package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourtsApiDelegateImpl test suite")
public class GetCourtLocationsTest {
    private final String COURTLEVEL = "COURTLEVEL";
    CourtsApiDelegateImpl sut;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sut = new CourtsApiDelegateImpl();
    }

    @Test
    @DisplayName("200")
    public void withValidCourtNameReturnCourtLocations() {

        ResponseEntity<CourtLocations> actual = sut.getCourtLocations(COURTLEVEL);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().getCourts().size());
        Assertions.assertEquals(BigDecimal.ONE, actual.getBody().getCourts().get(0).getId());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getName());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getCode());
        Assertions.assertEquals(true,actual.getBody().getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Test",actual.getBody().getCourts().get(0).getAddress().getCountryName());

    }
}
