package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapperImpl;
import ca.bc.gov.open.jag.efilingapi.error.CourtLocationException;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourtsApiDelegateImpl test suite")
public class GetCourtLocationsTest {

    private final String COURTLEVEL = "COURTLEVEL";
    private final String COURTLEVELERROR = "COURTLEVELERROR";

    CourtsApiDelegateImpl sut;



    @Mock
    EfilingCourtLocationService efilingCourtLocationServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(efilingCourtLocationServiceMock.getCourtLocations(COURTLEVEL)).thenReturn(buildMockData());

        Mockito.when(efilingCourtLocationServiceMock.getCourtLocations(COURTLEVELERROR)).thenReturn(null);

        sut = new CourtsApiDelegateImpl(efilingCourtLocationServiceMock, new CourtLocationMapperImpl());
    }

    @Test
    @DisplayName("200")
    public void withValidCourtNameReturnCourtLocations() {

        ResponseEntity<CourtLocations> actual = sut.getCourtLocations(COURTLEVEL);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(2, actual.getBody().getCourts().size());
        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.getBody().getCourts().get(0).getId());
        Assertions.assertEquals("Campbell River",actual.getBody().getCourts().get(0).getName());
        Assertions.assertEquals("MockCode",actual.getBody().getCourts().get(0).getCode());
        Assertions.assertEquals(true,actual.getBody().getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue",actual.getBody().getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null,actual.getBody().getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null,actual.getBody().getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1",actual.getBody().getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River",actual.getBody().getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia",actual.getBody().getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada",actual.getBody().getCourts().get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.getBody().getCourts().get(1).getId());
        Assertions.assertEquals("Chilliwack",actual.getBody().getCourts().get(1).getName());
        Assertions.assertEquals("MockCode",actual.getBody().getCourts().get(1).getCode());
        Assertions.assertEquals(true,actual.getBody().getCourts().get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road",actual.getBody().getCourts().get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ",actual.getBody().getCourts().get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ",actual.getBody().getCourts().get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8",actual.getBody().getCourts().get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack",actual.getBody().getCourts().get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia",actual.getBody().getCourts().get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada",actual.getBody().getCourts().get(1).getAddress().getCountryName());

    }

    @Test
    @DisplayName("500: with invalid court name should throw CourtLocationException")
    public void withInvalidCourtNameShouldThrowException() {

        CourtLocationException exception = Assertions.assertThrows(CourtLocationException.class, () -> sut.getCourtLocations(COURTLEVELERROR));
        Assertions.assertEquals(ErrorCode.COURT_LOCATION_ERROR.toString(), exception.getErrorCode());
    }

    private List<InternalCourtLocation> buildMockData() {
        InternalCourtLocation courtLocationOne = new InternalCourtLocation();
        courtLocationOne.setId(BigDecimal.valueOf(1031));
        courtLocationOne.setName("Campbell River");
        courtLocationOne.setCode("MockCode");
        courtLocationOne.setIsSupremeCourt(true);
        ca.bc.gov.open.jag.efilingcommons.model.Address addressOne = new ca.bc.gov.open.jag.efilingcommons.model.Address();
        addressOne.setAddressLine1("500 - 13th Avenue");
        addressOne.setPostalCode("V9W 6P1");
        addressOne.setCityName("Campbell River");
        addressOne.setProvinceName("British Columbia");
        addressOne.setCountryName("Canada");
        courtLocationOne.setAddress(addressOne);

        InternalCourtLocation courtLocationTwo = new InternalCourtLocation();
        courtLocationTwo.setId(BigDecimal.valueOf(3521));
        courtLocationTwo.setName("Chilliwack");
        courtLocationTwo.setCode("MockCode");
        courtLocationTwo.setIsSupremeCourt(true);
        ca.bc.gov.open.jag.efilingcommons.model.Address addressTwo = new ca.bc.gov.open.jag.efilingcommons.model.Address();
        addressTwo.setAddressLine1("46085 Yale Road");
        addressTwo.setAddressLine2("  ");
        addressTwo.setAddressLine3("  ");
        addressTwo.setPostalCode("V2P 2L8");
        addressTwo.setCityName("Chilliwack");
        addressTwo.setProvinceName("British Columbia");
        addressTwo.setCountryName("Canada");
        courtLocationTwo.setAddress(addressTwo);

        return Arrays.asList(courtLocationOne,courtLocationTwo);
    }
}
