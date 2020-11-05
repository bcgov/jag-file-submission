package ca.bc.gov.open.jag.efilingapi.lookup.lookupApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtInformation;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.lookup.LookupApiDelegateImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("LookupApiDelegateImpl test suite")
public class GetCourtLocationsTest {
    LookupApiDelegateImpl sut;

    @Mock
    private DocumentStore documentStoreMock;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sut = new LookupApiDelegateImpl(documentStoreMock);
    }

    @Test
    @DisplayName("200")
    public void withValidCourtNameReturnCourtLocations() {

        ResponseEntity<CourtLocations> actual = sut.getCourtLocations();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().getDocuments().size());
        Assertions.assertEquals(BigDecimal.ONE, actual.getBody().getDocuments().get(0).getCourtId());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCourtName());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCourtCode());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getTimeZone());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getDaylightSavings());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCourtIdentifierCode());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getAddressLine1());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getAddressLine2());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getAddressLine3());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getPostalCode());
        Assertions.assertEquals(BigDecimal.valueOf(123),actual.getBody().getDocuments().get(0).getCitySequenceNumber());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCityAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCityName());
        Assertions.assertEquals(BigDecimal.valueOf(123),actual.getBody().getDocuments().get(0).getProvinceSequenceNumber());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getProvinceAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getProvinceName());
        Assertions.assertEquals(BigDecimal.ONE,actual.getBody().getDocuments().get(0).getCountryId());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCountryAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getDocuments().get(0).getCountryName());
        Assertions.assertEquals(true,actual.getBody().getDocuments().get(0).getIsProvincialCourt());
        Assertions.assertEquals(true,actual.getBody().getDocuments().get(0).getIsSupremeCourt());
    }
}
