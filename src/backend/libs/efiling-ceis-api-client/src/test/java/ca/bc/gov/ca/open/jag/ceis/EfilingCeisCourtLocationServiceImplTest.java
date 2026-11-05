package ca.bc.gov.ca.open.jag.ceis;

import ca.bc.gov.open.jag.ceis.CeisCourtLocationMapperImpl;
import ca.bc.gov.open.jag.ceis.CeisCourtLocationServiceImpl;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtLocationServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingCeisCourtLocationServiceImplTest {

    private final String PROVINCIAL = "P";
    private final String SUPREME = "S";

    private CeisCourtLocationServiceImpl sut;

    @Mock
    private DefaultApi defaultApiMock;

    @BeforeEach
    public void beforeEach() throws ApiException {

        MockitoAnnotations.openMocks(this);

        sut = new CeisCourtLocationServiceImpl(defaultApiMock, new CeisCourtLocationMapperImpl());
    }

    @Test
    @DisplayName("OK: Get Provincial")
    public void withPAsParameterReturnOnlyProvincial() throws ApiException {

        Mockito.when(defaultApiMock.courtLocationsGet()).thenReturn(buildMockData());

        List<InternalCourtLocation> actual = sut.getCourtLocations(PROVINCIAL);

        Assertions.assertEquals(2, actual.size());

        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.get(0).getId());
        Assertions.assertEquals("Campbell River", actual.get(0).getName());
        Assertions.assertEquals("MockCode", actual.get(0).getCode());
        Assertions.assertEquals(true, actual.get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.get(1).getId());
        Assertions.assertEquals("Chilliwack", actual.get(1).getName());
        Assertions.assertEquals("MockCode", actual.get(1).getCode());
        Assertions.assertEquals(false, actual.get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road", actual.get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8", actual.get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack", actual.get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(1).getAddress().getCountryName());

    }

    @Test
    @DisplayName("OK: Get Supreme")
    public void withSAsParameterReturnOnlySupreme() throws ApiException {

        Mockito.when(defaultApiMock.courtLocationsGet()).thenReturn(buildMockData());

        List<InternalCourtLocation> actual = sut.getCourtLocations(SUPREME);

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.get(0).getId());
        Assertions.assertEquals("Campbell River", actual.get(0).getName());
        Assertions.assertEquals("MockCode", actual.get(0).getCode());
        Assertions.assertEquals(true, actual.get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(0).getAddress().getCountryName());

    }

    @Test
    @DisplayName("OK: Get All")
    public void withNullAsParameterReturnAll() throws ApiException {

        Mockito.when(defaultApiMock.courtLocationsGet()).thenReturn(buildMockData());

        List<InternalCourtLocation> actual = sut.getCourtLocations(null);

        Assertions.assertEquals(2, actual.size());

        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.get(0).getId());
        Assertions.assertEquals("Campbell River", actual.get(0).getName());
        Assertions.assertEquals("MockCode", actual.get(0).getCode());
        Assertions.assertEquals(true, actual.get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.get(1).getId());
        Assertions.assertEquals("Chilliwack", actual.get(1).getName());
        Assertions.assertEquals("MockCode", actual.get(1).getCode());
        Assertions.assertEquals(false, actual.get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road", actual.get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8", actual.get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack", actual.get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(1).getAddress().getCountryName());

    }


    private CourtLocations buildMockData() {

        CourtLocation courtLocationOne = new ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation();
        courtLocationOne.setCourtid(BigDecimal.valueOf(1031));
        courtLocationOne.setCourtname("Campbell River");
        courtLocationOne.setCourtcode("MockCode");
        courtLocationOne.setIssupremecourt("Y");
        courtLocationOne.setIsprovincialcourt("Y");
        courtLocationOne.setAddressline1("500 - 13th Avenue");
        courtLocationOne.setPostalcode("V9W 6P1");
        courtLocationOne.setCityname("Campbell River");
        courtLocationOne.setProvincename("British Columbia");
        courtLocationOne.setCountryname("Canada");


        CourtLocation courtLocationTwo = new ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation();
        courtLocationTwo.setCourtid(BigDecimal.valueOf(3521));
        courtLocationTwo.setCourtname("Chilliwack");
        courtLocationTwo.setCourtcode("MockCode");
        courtLocationTwo.setIssupremecourt("N");
        courtLocationTwo.setIsprovincialcourt("Y");
        courtLocationTwo.setAddressline1("46085 Yale Road");
        courtLocationTwo.setAddressline2("  ");
        courtLocationTwo.setAddressline3("  ");
        courtLocationTwo.setPostalcode("V2P 2L8");
        courtLocationTwo.setCityname("Chilliwack");
        courtLocationTwo.setProvincename("British Columbia");
        courtLocationTwo.setCountryname("Canada");

        CourtLocations courtLocations = new CourtLocations();
        courtLocations.setCourtlocations(Arrays.asList(courtLocationOne,courtLocationTwo));
        return courtLocations;

    }

    @Test
    @DisplayName("Error: return null")
    public void withExceptionReturnNull() throws ApiException {

        Mockito.when(defaultApiMock.courtLocationsGet()).thenThrow(new ApiException());
        Assertions.assertThrows(EfilingCourtLocationServiceException.class, () -> sut.getCourtLocations(null));

    }

}
