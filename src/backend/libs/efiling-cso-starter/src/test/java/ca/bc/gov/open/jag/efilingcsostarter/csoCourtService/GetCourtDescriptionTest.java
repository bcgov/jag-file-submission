package ca.bc.gov.open.jag.efilingcsostarter.csoCourtService;

import ca.bc.gov.ag.csows.ceis.CsoAgencyArr;
import ca.bc.gov.ag.csows.ceis.CsoAgencyRec;
import ca.bc.gov.ag.csows.ceis.Csows;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcsostarter.CsoCourtServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetCourtDescriptionTest {
    private static final String NOVALUE = "NOVALUE";
    private static final String AGEN_AGENCY_NM = "FOUNDAGENCY";
    private static final String AGEN_AGENCY_IDENTIFIER_CD = "FOUND";
    private static final String AGEN_AGENCY_IDENTIFIER_CD1 = "OTHER";
    private static final String AGEN_AGENCY_NM1 = "OTHERAGENCY";
    private static final String COURT_LEVEL = "LEVEL";
    private static final String COURT_CLASS = "CLASS";
    private static final String AGENCY = "AGENCY";
    @Mock
    Csows csowsMock;

    @Mock
    CsoAgencyArr csoAgencyArrMock;


    private static CsoCourtServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        CsoAgencyRec csoAgencyRec1 = new CsoAgencyRec();
        csoAgencyRec1.setAgenAgencyIdentifierCd(AGEN_AGENCY_IDENTIFIER_CD);
        csoAgencyRec1.setAgenAgencyNm(AGEN_AGENCY_NM);
        csoAgencyRec1.setAgenId(BigDecimal.TEN);
        CsoAgencyRec csoAgencyRec2 = new CsoAgencyRec();
        csoAgencyRec2.setAgenAgencyIdentifierCd(AGEN_AGENCY_IDENTIFIER_CD1);
        csoAgencyRec2.setAgenAgencyNm(AGEN_AGENCY_NM1);
        csoAgencyRec2.setAgenId(BigDecimal.TEN);
        Mockito.when(csoAgencyArrMock.getArray()).thenReturn(Arrays.asList(csoAgencyRec1, csoAgencyRec2));
        Mockito.when(csowsMock.getCourtLocations()).thenReturn(csoAgencyArrMock);

        sut = new CsoCourtServiceImpl(csowsMock);
    }
    @DisplayName("Exception: with null parameter throws Illegal Argument exception")
    @Test
    public void withNullParameterThrowsIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getCourtDescription(null, COURT_LEVEL, COURT_CLASS));

    }
    @DisplayName("Exception: with null parameter throws Illegal Argument exception")
    @Test
    public void withNullLevelParameterThrowsIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getCourtDescription(AGENCY, null, COURT_CLASS));

    }
    @DisplayName("Exception: with null parameter throws Illegal Argument exception")
    @Test
    public void withNullClassParameterThrowsIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getCourtDescription(AGENCY, COURT_LEVEL, null));

    }
    @DisplayName("Ok: with valid string and existing location return description")
    @Test
    public void withValidStringAndExistingLocationReturnDescription() {

        CourtDetails result = sut.getCourtDescription(AGEN_AGENCY_IDENTIFIER_CD, COURT_LEVEL, COURT_CLASS);

        Assertions.assertEquals(AGEN_AGENCY_NM, result.getCourtDescription());
        Assertions.assertEquals(BigDecimal.TEN, result.getCourtId());

    }
    @DisplayName("Exception: with no result should throw EfilingLookupServiceException")
    @Test
    public void withNoFoundResultShouldThrowEfilingLookupServiceException() {

        Assertions.assertThrows(EfilingCourtServiceException.class, () -> sut.getCourtDescription(NOVALUE));

    }
}
