package ca.bc.gov.open.jag.efilingapi.court;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtServiceImplTest {

    private static final String COURT_CLASSIFICATION = "classification";
    private static final String COURT_LEVEL = "level";
    private static final String COURT_LOCATION = "location";
    public static final String CLASS_DESCRIPTION = "classDescription";
    public static final String COURT_DESCRIPTION = "courtDescription";
    public static final String LEVEL_DESCRIPTION = "levelDescription";
    public static final BigDecimal COURT_ID = BigDecimal.TEN;

    private CourtServiceImpl sut;

    @Mock
    private EfilingCourtService efilingCourtServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.initMocks(this);

        CourtDetails courtDetails = new CourtDetails(COURT_ID, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);
        Mockito.when(efilingCourtServiceMock.getCourtDescription(
                Mockito.eq(COURT_LOCATION),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASSIFICATION))).thenReturn(courtDetails);

        sut = new CourtServiceImpl(efilingCourtServiceMock);
    }


    @Test
    @DisplayName("ok: with valid request should return court description")
    public void withValidRequestShouldReturnCourtDescription() {

        CourtDetails actual = sut.getCourtDetails(GetCourtDetailsRequest
                .builder()
                .courtClassification(COURT_CLASSIFICATION)
                .courtLevel(COURT_LEVEL)
                .courtLocation(COURT_LOCATION)
                .create());

        Assertions.assertEquals(COURT_ID, actual.getCourtId());
        Assertions.assertEquals(CLASS_DESCRIPTION, actual.getClassDescription());
        Assertions.assertEquals(COURT_DESCRIPTION, actual.getCourtDescription());
        Assertions.assertEquals(LEVEL_DESCRIPTION, actual.getLevelDescription());

    }

}
