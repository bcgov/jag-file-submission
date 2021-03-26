package ca.bc.gov.open.jag.efilingcsoclient.csoSearchServiceImpl;

import ca.bc.gov.courts.appeal.ws.services.COACase;
import ca.bc.gov.courts.appeal.ws.services.CSOSearchSoap;
import ca.bc.gov.open.jag.efilingcsoclient.CsoSearchServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Search Service Test")
public class CaseNumberExistsTest {

    public static final String CASE_NUMBER = "TEST";
    CsoSearchServiceImpl sut;

    @Mock
    CSOSearchSoap csoSearchSoapMock;
    
    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new CsoSearchServiceImpl(csoSearchSoapMock);

    }

    @DisplayName("Exception: with null case number, service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyCaseNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.caseNumberExists(null));
    }

    @DisplayName("OK: existing caseNumber")
    @Test
    public void testExistingCaseNumber() {

        Mockito.when(csoSearchSoapMock.searchByCaseNumber(any())).thenReturn(new COACase());
        Assertions.assertTrue(sut.caseNumberExists(CASE_NUMBER));

    }

    @DisplayName("OK: non existing caseNumber")
    @Test
    public void testNonExistingCaseNumber() {

        Mockito.when(csoSearchSoapMock.searchByCaseNumber(any())).thenReturn(null);
        Assertions.assertFalse(sut.caseNumberExists(CASE_NUMBER));

    }

}
