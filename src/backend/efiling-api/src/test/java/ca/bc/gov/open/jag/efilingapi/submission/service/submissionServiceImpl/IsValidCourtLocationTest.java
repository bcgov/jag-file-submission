package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Is Valid Court Location Test suite")
public class IsValidCourtLocationTest {


    public static final String APP_1 = "App1";
    public static final String COURT_CLASS = "court class";
    public static final BigDecimal COURT_ID = BigDecimal.ONE;
    public static final String COURT_LEVEL = "courtLevel";
    public static final String APP_2 = "App2";
    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private EfilingLookupService efilingLookupService;

    @Mock
    private EfilingCourtService efilingCourtService;

    @Mock
    private EfilingDocumentService efilingDocumentService;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private EfilingSubmissionService efilingSubmissionServiceMock;

    @Mock
    private BamboraPaymentAdapter bamboraPaymentAdapterMock;

    @Mock
    private SftpService sftpServiceMock;


    @BeforeAll
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(efilingCourtService.checkValidLevelClassLocation(
                Mockito.eq(COURT_ID),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASS),
                Mockito.eq(APP_1)))
                .thenReturn(true);
        Mockito.when(efilingCourtService.checkValidLevelClassLocation(
                Mockito.eq(COURT_ID),
                Mockito.eq(COURT_LEVEL),
                Mockito.eq(COURT_CLASS),
                Mockito.eq(APP_2)))
                .thenReturn(false);
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, new PartyMapperImpl(), efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock, bamboraPaymentAdapterMock, sftpServiceMock, efilingDocumentService);
    }


    @Test
    @DisplayName("ok: with valid combination should return true")
    public void WithValidCombinationShouldBeValid() {


        IsValidCourtRequest request = IsValidCourtRequest
                .builder()
                .applicationCode(APP_1)
                .courtClassification(COURT_CLASS)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .create();
        Boolean actual = sut.isValidCourtLocation(request);

        Assertions.assertTrue(actual);

    }

    @Test
    @DisplayName("ok: with valid combination should return false")
    public void WithValidCombinationShouldBeInValid() {

        IsValidCourtRequest request = IsValidCourtRequest
                .builder()
                .applicationCode(APP_2)
                .courtClassification(COURT_CLASS)
                .courtId(COURT_ID)
                .courtLevel(COURT_LEVEL)
                .create();

        Boolean actual = sut.isValidCourtLocation(request);

        Assertions.assertFalse(actual);

    }

}
