package ca.bc.gov.open.jag.efilingapi.court.services;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtFileNumberRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtServiceImplTest {

    private static final String COURT_CLASSIFICATION = "classification";
    private static final String COURT_LEVEL = "level";
    private static final String COURT_OF_APPEAL_LEVEL = "a";
    private static final String COURT_LOCATION = "location";
    public static final String CLASS_DESCRIPTION = "classDescription";
    public static final String COURT_DESCRIPTION = "courtDescription";
    public static final String LEVEL_DESCRIPTION = "levelDescription";
    public static final BigDecimal COURT_ID = BigDecimal.TEN;
    public static final String CASE_1 = "case1";
    public static final String CASE_2 = "case2";
    public static final String FILE_NUMBER = "123465";

    private CourtServiceImpl sut;

    @Mock
    private EfilingCourtService efilingCourtServiceMock;
    @Mock
    private EfilingSearchService efilingSearchServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        CourtDetails courtDetails = new CourtDetails(COURT_ID, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);
        Mockito.when(efilingCourtServiceMock.getCourtDescription(
                Mockito.eq(COURT_LOCATION),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASSIFICATION))).thenReturn(Optional.of(courtDetails));

        Mockito.when(efilingCourtServiceMock
                .checkValidLevelClassLocation(
                        Mockito.eq(COURT_ID),
                        Mockito.eq(COURT_LEVEL),
                        Mockito.eq(COURT_CLASSIFICATION),
                        Mockito.eq(CASE_1)
                )).thenReturn(true);

        Mockito.when(efilingCourtServiceMock
                .checkValidLevelClassLocation(
                        Mockito.eq(COURT_ID),
                        Mockito.eq(COURT_LEVEL),
                        Mockito.eq(COURT_CLASSIFICATION),
                        Mockito.eq(CASE_2)
                )).thenReturn(false);

        Mockito.when(efilingCourtServiceMock
        .checkValidCourtFileNumber(
                Mockito.eq(FILE_NUMBER),
                Mockito.eq(COURT_ID),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASSIFICATION),
                Mockito.eq(CASE_1)
        )).thenReturn(true);

        Mockito.when(efilingCourtServiceMock
        .checkValidCourtFileNumber(
                Mockito.eq(FILE_NUMBER),
                Mockito.eq(COURT_ID),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASSIFICATION),
                Mockito.eq(CASE_2)
        )).thenReturn(false);

        Mockito.when(efilingSearchServiceMock.caseNumberExists(Mockito.eq(FILE_NUMBER))).thenReturn(true);
        sut = new CourtServiceImpl(efilingCourtServiceMock, efilingSearchServiceMock);

    }


    @Test
    @DisplayName("ok: with valid request should return court description")
    public void withValidRequestShouldReturnCourtDescription() {

        CourtDetails actual = sut.getCourtDetails(GetCourtDetailsRequest
                .builder()
                .courtClassification(COURT_CLASSIFICATION)
                .courtLevel(COURT_LEVEL)
                .courtLocation(COURT_LOCATION)
                .create()).get();

        Assertions.assertEquals(COURT_ID, actual.getCourtId());
        Assertions.assertEquals(CLASS_DESCRIPTION, actual.getClassDescription());
        Assertions.assertEquals(COURT_DESCRIPTION, actual.getCourtDescription());
        Assertions.assertEquals(LEVEL_DESCRIPTION, actual.getLevelDescription());

    }

    @Test
    @DisplayName("ok: should validate court classification and return true")
    public void shouldValidateCourtClassificationAndReturnTrue() {

        boolean actual = sut.isValidCourt(IsValidCourtRequest.builder()
                .applicationCode(CASE_1)
                .courtClassification(COURT_CLASSIFICATION)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .create());

        Assertions.assertTrue(actual);

    }

    @Test
    @DisplayName("ok: should validate court classification and return false")
    public void shouldValidateCourtClassificationAndReturnFalse() {

        boolean actual = sut.isValidCourt(IsValidCourtRequest.builder()
                .applicationCode(CASE_2)
                .courtClassification(COURT_CLASSIFICATION)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .create());

        Assertions.assertFalse(actual);


    }

    @Test
    @DisplayName("ok: should validate a court file number and return true")
    public void shouldValidateACourtFileNumberAndReturnTrue() {

        boolean actual = sut.isValidCourtFileNumber(IsValidCourtFileNumberRequest.builder()
                .applicationCode(CASE_1)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .courtClassification(COURT_CLASSIFICATION)
                .fileNumber(FILE_NUMBER).create());

        Assertions.assertTrue(actual);

    }

    @Test
    @DisplayName("ok: with Court of Appeal should validate a court file number and return true")
    public void withCourtOfAppealShouldValidateACourtFileNumberAndReturnTrue() {

        boolean actual = sut.isValidCourtFileNumber(IsValidCourtFileNumberRequest.builder()
                .applicationCode(CASE_1)
                .courtId(COURT_ID)
                .courtLevel(COURT_OF_APPEAL_LEVEL)
                .courtClassification(COURT_CLASSIFICATION)
                .fileNumber(FILE_NUMBER).create());

        Assertions.assertTrue(actual);

    }

    @Test
    @DisplayName("ok: should validate a court file number and return false")
    public void shouldValidateACourtFileNumberAndReturnFalse() {

        boolean actual = sut.isValidCourtFileNumber(IsValidCourtFileNumberRequest.builder()
                .applicationCode(CASE_2)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .courtClassification(COURT_CLASSIFICATION)
                .fileNumber(FILE_NUMBER).create());

        Assertions.assertFalse(actual);

    }

}
