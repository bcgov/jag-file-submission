package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.EfilingFilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
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
import static org.mockito.ArgumentMatchers.anyBoolean;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class createSubmissionTest {

    public static final String INTERNAL_CLIENT_NUMBER = "12345";
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
    private BamboraPaymentAdapter bamboraPaymentAdapterMock;

    @Mock
    private SftpService sftpServiceMock;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(efilingSubmissionServiceMock.submitFilingPackage(any(), any(), any(), anyBoolean(), any())).thenReturn(BigDecimal.TEN);
        Mockito.when(bamboraPaymentAdapterMock.makePayment(any())).thenReturn(new EfilingTransaction());
        Mockito.when(documentStoreMock.get(any(), any())).thenReturn(new byte[]{});
        Mockito.doNothing().when(sftpServiceMock).put(any(), any());
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, new PartyMapperImpl(), new EfilingFilingPackageMapperImpl(),efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock, bamboraPaymentAdapterMock, sftpServiceMock);

    }


    @Test
    @DisplayName("OK: service is created")
    public void withValidSubmissionServiceIsCreated() {

        SubmitResponse actual = sut.createSubmission(Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .accountDetails(AccountDetails.builder()
                        .clientId(BigDecimal.TEN)
                        .accountId(BigDecimal.TEN)
                        .internalClientNumber("123")
                        .create())
                .transactionId(TestHelpers.CASE_1)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .clientApplication(TestHelpers.createClientApplication(TestHelpers.DISPLAY_NAME, TestHelpers.TYPE))
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
        assertEquals(BigDecimal.TEN, actual.getTransactionId());
    }

}
