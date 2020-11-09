package ca.bc.gov.open.jag.efilingapi.courts.adapter;

import ca.bc.gov.open.jag.efilingapi.api.model.Address;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.courts.CeisLookupAdapter;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapperImpl;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CeisLookupAdapter test suite")
public class CeisLookupAdapterTest {
    private final String PROVINCIAL = "P";
    private final String SUPREME = "S";


    CeisLookupAdapter sut;

    @Mock
    DefaultApi defaultApiMock;

    @Mock
    CourtLocationMapper courtLocationMapperMock;

    @BeforeAll
    public void setUp() throws ApiException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(defaultApiMock.courtLocationsGet()).thenReturn(buildMockData());

        courtLocationMapperMock = new CourtLocationMapperImpl();

        sut = new CeisLookupAdapter(defaultApiMock, courtLocationMapperMock);
    }

    @Test
    @DisplayName("Get Provincial")
    public void withPAsParameterReturnOnlyProvincial() throws ApiException {

        CourtLocations actual = sut.getCourLocations(PROVINCIAL);

        Assertions.assertEquals(2, actual.getCourts().size());

        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.getCourts().get(0).getId());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getName());
        Assertions.assertEquals("MockCode", actual.getCourts().get(0).getCode());
        Assertions.assertEquals(true, actual.getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.getCourts().get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.getCourts().get(1).getId());
        Assertions.assertEquals("Chilliwack", actual.getCourts().get(1).getName());
        Assertions.assertEquals("MockCode", actual.getCourts().get(1).getCode());
        Assertions.assertEquals(false, actual.getCourts().get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road", actual.getCourts().get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ", actual.getCourts().get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ", actual.getCourts().get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8", actual.getCourts().get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack", actual.getCourts().get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.getCourts().get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.getCourts().get(1).getAddress().getCountryName());

    }

    @Test
    @DisplayName("Get Supreme")
    public void withSAsParameterReturnOnlySupreme() throws ApiException {

        CourtLocations actual = sut.getCourLocations(SUPREME);

        Assertions.assertEquals(1, actual.getCourts().size());
        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.getCourts().get(0).getId());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getName());
        Assertions.assertEquals("MockCode", actual.getCourts().get(0).getCode());
        Assertions.assertEquals(true, actual.getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.getCourts().get(0).getAddress().getCountryName());

    }

    @Test
    @DisplayName("Get All")
    public void withNullAsParameterReturnAll() throws ApiException {

        CourtLocations actual = sut.getCourLocations(null);

        Assertions.assertEquals(2, actual.getCourts().size());

        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.getCourts().get(0).getId());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getName());
        Assertions.assertEquals("MockCode", actual.getCourts().get(0).getCode());
        Assertions.assertEquals(true, actual.getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null, actual.getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.getCourts().get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.getCourts().get(1).getId());
        Assertions.assertEquals("Chilliwack", actual.getCourts().get(1).getName());
        Assertions.assertEquals("MockCode", actual.getCourts().get(1).getCode());
        Assertions.assertEquals(false, actual.getCourts().get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road", actual.getCourts().get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ", actual.getCourts().get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ", actual.getCourts().get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8", actual.getCourts().get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack", actual.getCourts().get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.getCourts().get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.getCourts().get(1).getAddress().getCountryName());

    }

    private List<ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation> buildMockData() {

        ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation courtLocationOne = new ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation();
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


        ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation courtLocationTwo = new ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation();
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

        return Arrays.asList(courtLocationOne,courtLocationTwo);

    }

}
