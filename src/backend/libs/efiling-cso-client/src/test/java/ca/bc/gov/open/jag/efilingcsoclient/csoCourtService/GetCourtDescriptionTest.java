package ca.bc.gov.open.jag.efilingcsoclient.csoCourtService;

import ca.bc.gov.ag.csows.ceis.*;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingCourtServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcsoclient.CsoCourtServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
    private static final String LEVELCD_1 = "LEVELCD1";
    private static final String LEVEL_1 = "LEVEL1";
    private static final String LEVELCD_2 = "LEVELCD2";
    private static final String LEVEL_2 = "LEVEL2";
    private static final String CLASSCD_1 = "CLASSCD1";
    private static final String CLASS_1 = "CLASS1";
    private static final String CLASSCD_2 = "CLASSCD2";
    private static final String CLASS_2 = "CLASS2";
    @Mock
    Csows csowsMock;

    @Mock
    FilingStatusFacadeBean filingStatusFacadeBeanMock;

    @Mock
    CsoAgencyArr csoAgencyArrMock;


    @Mock
    CsoCourtLevelArr csoCourtLevelArrMock;


    @Mock
    CsoCourtClassArr csoCourtClassArrMock;


    private static CsoCourtServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(csoCourtLevelArrMock.getArray()).thenReturn(createLevelArray());
        Mockito.when(csowsMock.getCourtLevels()).thenReturn(csoCourtLevelArrMock);

        Mockito.when(csoCourtClassArrMock.getArray()).thenReturn(createClassArray());
        Mockito.when(csowsMock.getCourtClasses(any())).thenReturn(csoCourtClassArrMock);

        Mockito.when(csoAgencyArrMock.getArray()).thenReturn(createAgencyArray());
        Mockito.when(csowsMock.getCourtLocations()).thenReturn(csoAgencyArrMock);

        sut = new CsoCourtServiceImpl(csowsMock, filingStatusFacadeBeanMock);
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
    public void withValidStringAndExistingLocationReturnDescriptions() {

        CourtDetails actual = sut.getCourtDescription(AGEN_AGENCY_IDENTIFIER_CD, LEVELCD_1, CLASSCD_1).get();

        Assertions.assertEquals(AGEN_AGENCY_NM, actual.getCourtDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCourtId());
        Assertions.assertEquals(CLASS_1, actual.getClassDescription());
        Assertions.assertEquals(LEVEL_1, actual.getLevelDescription());

    }

    @DisplayName("Exception: with missing level throw exception")
    @Test
    public void withValidStringAndMissingLevelReturnDescriptions() {

        Assertions.assertThrows(EfilingCourtServiceException.class, () ->  sut.getCourtDescription(AGEN_AGENCY_IDENTIFIER_CD, COURT_LEVEL, CLASSCD_1));

    }

    @DisplayName("Exception: with missing class throw exception")
    @Test
    public void withValidStringAndMissingClassReturnDescriptions() {

        Assertions.assertThrows(EfilingCourtServiceException.class, () ->  sut.getCourtDescription(AGEN_AGENCY_IDENTIFIER_CD, LEVELCD_1, COURT_CLASS));

    }

    @DisplayName("Ok: with no result should return Empty")
    @Test
    public void withNoFoundResultShouldReturnEmpty() {

        Optional<CourtDetails> actual = sut.getCourtDescription(NOVALUE, COURT_LEVEL, COURT_CLASS);

        Assertions.assertFalse(actual.isPresent());

    }

    private List<CsoAgencyRec> createAgencyArray() {
        CsoAgencyRec csoAgencyRec1 = new CsoAgencyRec();
        csoAgencyRec1.setAgenAgencyIdentifierCd(AGEN_AGENCY_IDENTIFIER_CD);
        csoAgencyRec1.setAgenAgencyNm(AGEN_AGENCY_NM);
        csoAgencyRec1.setAgenId(BigDecimal.TEN);
        CsoAgencyRec csoAgencyRec2 = new CsoAgencyRec();
        csoAgencyRec2.setAgenAgencyIdentifierCd(AGEN_AGENCY_IDENTIFIER_CD1);
        csoAgencyRec2.setAgenAgencyNm(AGEN_AGENCY_NM1);
        csoAgencyRec2.setAgenId(BigDecimal.TEN);
        return Arrays.asList(csoAgencyRec1, csoAgencyRec2);
    }

    private List<CsoCourtLevelRec> createLevelArray() {
        CsoCourtLevelRec csoLevelRec1 = new CsoCourtLevelRec();
        csoLevelRec1.setLevelCd(LEVELCD_1);
        csoLevelRec1.setLevelDsc(LEVEL_1);
        CsoCourtLevelRec csoLevelRec2 = new CsoCourtLevelRec();
        csoLevelRec2.setLevelCd(LEVELCD_2);
        csoLevelRec2.setLevelDsc(LEVEL_2);
        return Arrays.asList(csoLevelRec1, csoLevelRec2);
    }

    private List<CsoCourtClassRec> createClassArray() {
        CsoCourtClassRec csoClassRec1 = new CsoCourtClassRec();
        csoClassRec1.setClassCd(CLASSCD_1);
        csoClassRec1.setClassDsc(CLASS_1);
        CsoCourtClassRec csoClassRec2 = new CsoCourtClassRec();
        csoClassRec2.setClassCd(CLASSCD_2);
        csoClassRec2.setClassDsc(CLASS_2);
        return Arrays.asList(csoClassRec1, csoClassRec2);
    }


}
