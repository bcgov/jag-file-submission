package ca.bc.gov.open.jag.efilingcsoclient.csoLookupService;

import ca.bc.gov.ag.csows.lookups.CodeValue;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import ca.bc.gov.open.jag.efilingcsoclient.CsoLookupServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Get Countries Test")
public class GetCountriesTest {

    private static final String TEST = "TEST";
    private static final String TEST_1 = "TEST_1";
    private static final String TEST_2 = "TEST_2";

    CsoLookupServiceImpl sut;

    @Mock
    LookupFacadeBean lookupFacadeBeanMock;

    @BeforeEach
    public void beforeEach() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);

        sut = new CsoLookupServiceImpl(lookupFacadeBeanMock);

    }

    @DisplayName("OK: getCountries called ")
    @Test
    public void testGetCountriesNoError() throws NestedEjbException_Exception {

        Mockito
                .when(lookupFacadeBeanMock.getCountries())
                .thenReturn(createCodeList());

        List<LookupItem> actual = sut.getCountries();
        Assertions.assertEquals(2, actual.size());

        Assertions.assertEquals(TEST_1, actual.get(0).getCode());
        Assertions.assertEquals(TEST, actual.get(0).getDescription());

        Assertions.assertEquals(TEST_2, actual.get(1).getCode());
        Assertions.assertEquals(TEST, actual.get(1).getDescription());

    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingLookupServiceException() throws NestedEjbException_Exception {


        Mockito
                .when(lookupFacadeBeanMock.getCountries())
                .thenThrow(new NestedEjbException_Exception("random"));

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getCountries());

    }

    private List<CodeValue> createCodeList() {

        List<CodeValue> codes = new ArrayList<>();
        CodeValue codeOne = new CodeValue();
        codeOne.setCode(TEST_1);
        codeOne.setDescription(GetCountriesTest.TEST);

        CodeValue codeTwo = new CodeValue();
        codeTwo.setCode(TEST_2);
        codeTwo.setDescription(TEST);

        return Arrays.asList(codeOne, codeTwo);

    }

}

