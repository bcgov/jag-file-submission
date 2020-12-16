package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateSubmissionTest {

    public static final String INTERNAL_CLIENT_NUMBER = "12345";
    public static final String CLIENT_APP_NAME = "clientAppName";
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
    private DocumentStore documentStoreMock;

    @Mock
    private EfilingSubmissionService efilingSubmissionServiceMock;

    @Mock
    private PaymentAdapter paymentAdapterMock;

    @Mock
    private SftpService sftpServiceMock;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(efilingSubmissionServiceMock.submitFilingPackage(any(), any(), any())).thenReturn(SubmitPackageResponse.builder().transactionId(BigDecimal.TEN).packageLink("http://link").create());
        Mockito.when(paymentAdapterMock.makePayment(any())).thenReturn(new PaymentTransaction());
        Mockito.when(documentStoreMock.get(any(), any())).thenReturn(new byte[]{});
        Mockito.doNothing().when(sftpServiceMock).put(any(), any());
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, new PartyMapperImpl(), efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock, paymentAdapterMock, sftpServiceMock);

    }


    @Test
    @DisplayName("OK: service is created")
    public void withValidSubmissionServiceIsCreated() {

        SubmitResponse actual = sut.createSubmission(Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .transactionId(TestHelpers.CASE_1)
                .navigationUrls(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .clientAppName(CLIENT_APP_NAME)
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList()))
                .create(),
                AccountDetails.builder()
                        .fileRolePresent(true)
                        .accountId(BigDecimal.ONE)
                        .cardRegistered(true)
                        .universalId(UUID.randomUUID())
                        .clientId(BigDecimal.TEN)
                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                        .create());
        assertEquals("aHR0cDovL2xpbms=", actual.getPackageRef());
    }

}
