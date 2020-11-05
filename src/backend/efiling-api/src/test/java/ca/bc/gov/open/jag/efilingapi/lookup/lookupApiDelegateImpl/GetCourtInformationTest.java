package ca.bc.gov.open.jag.efilingapi.lookup.lookupApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtInformation;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.lookup.LookupApiDelegateImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("LookupApiDelegateImpl test suite")
public class GetCourtInformationTest {
    private static final String COURTNAME = "COURTNAME";
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
    public void withValidCourtNameReturnCourtInformation() {

        ResponseEntity<CourtInformation> actual = sut.getCourtInformation(COURTNAME);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(BigDecimal.ONE, actual.getBody().getCourtId());
        Assertions.assertEquals("Test",actual.getBody().getCourtName());
        Assertions.assertEquals("Test",actual.getBody().getCourtCode());
        Assertions.assertEquals("Test",actual.getBody().getTimeZone());
        Assertions.assertEquals("Test",actual.getBody().getDaylightSavings());
        Assertions.assertEquals("Test",actual.getBody().getCourtIdentifierCode());
        Assertions.assertEquals("Test",actual.getBody().getAddressLine1());
        Assertions.assertEquals("Test",actual.getBody().getAddressLine2());
        Assertions.assertEquals("Test",actual.getBody().getAddressLine3());
        Assertions.assertEquals("Test",actual.getBody().getPostalCode());
        Assertions.assertEquals(BigDecimal.valueOf(123),actual.getBody().getCitySequenceNumber());
        Assertions.assertEquals("Test",actual.getBody().getCityAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getCityName());
        Assertions.assertEquals(BigDecimal.valueOf(123),actual.getBody().getProvinceSequenceNumber());
        Assertions.assertEquals("Test",actual.getBody().getProvinceAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getProvinceName());
        Assertions.assertEquals(BigDecimal.ONE,actual.getBody().getCountryId());
        Assertions.assertEquals("Test",actual.getBody().getCountryAbbreviation());
        Assertions.assertEquals("Test",actual.getBody().getCountryName());
        Assertions.assertEquals(true,actual.getBody().getIsProvincialCourt());
        Assertions.assertEquals(true,actual.getBody().getIsSupremeCourt());
    }
}
