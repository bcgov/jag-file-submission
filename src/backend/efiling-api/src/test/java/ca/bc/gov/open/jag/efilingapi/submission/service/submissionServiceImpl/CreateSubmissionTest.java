package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
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
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateSubmissionTest {

    public static final String INTERNAL_CLIENT_NUMBER = "12345";
    public static final String CLIENT_APP_NAME = "clientAppName";
    private static final String FILE_PDF = "file.pdf";
    private static final String FILE_1_PDF = "file1.pdf";
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
        Mockito.when(efilingSubmissionServiceMock.submitFilingPackage(any(), any(), any())).thenReturn(SubmitPackageResponse.builder().transactionId(BigDecimal.valueOf(11000)).packageLink("http://link").create());
        Mockito.when(paymentAdapterMock.makePayment(any())).thenReturn(new PaymentTransaction());
        Mockito.when(documentStoreMock.get(any(), any())).thenReturn(new byte[]{});
        Mockito.when(documentStoreMock.getRushDocument(any(), any())).thenReturn(new byte[]{});
        Mockito.doNothing().when(sftpServiceMock).put(any(), any());
        NavigationProperties navigationProperties = new NavigationProperties();
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, new PartyMapperImpl(), efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, null, documentStoreMock, paymentAdapterMock, sftpServiceMock, navigationProperties);

    }


    @Test
    @DisplayName("OK: service is created without early adopter")
    public void withValidSubmissionServiceIsCreatedNotEarlyAdopter() {

        SubmitResponse actual = sut.createSubmission(Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .universalId(UUID.randomUUID().toString())
                .transactionId(TestHelpers.CASE_1)
                .navigationUrls(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .clientAppName(CLIENT_APP_NAME)
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
                .create(),
                AccountDetails.builder()
                        .fileRolePresent(true)
                        .accountId(BigDecimal.ONE)
                        .cardRegistered(true)
                        .universalId(UUID.randomUUID().toString())
                        .clientId(BigDecimal.TEN)
                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                        .create(), false);
        assertEquals("aHR0cDovL2xpbms=", actual.getPackageRef());
    }

    @Test
    @DisplayName("OK: service is created with early adopter")
    public void withValidSubmissionServiceIsCreatedEarlyAdopter() {

        SubmitResponse actual = sut.createSubmission(Submission
                        .builder()
                        .id(TestHelpers.CASE_1)
                        .transactionId(TestHelpers.CASE_1)
                        .universalId(UUID.randomUUID().toString())
                        .navigationUrls(TestHelpers.createDefaultNavigation())
                        .expiryDate(10)
                        .clientAppName(CLIENT_APP_NAME)
                        .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
                        .create(),
                AccountDetails.builder()
                        .fileRolePresent(true)
                        .accountId(BigDecimal.ONE)
                        .cardRegistered(true)
                        .universalId(UUID.randomUUID().toString())
                        .clientId(BigDecimal.TEN)
                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                        .create(), true);
        assertEquals("bnVsbC9wYWNrYWdlcmV2aWV3LzExMDAwP3BhY2thZ2VObz0xMTAwMA==", actual.getPackageRef());
    }

    @Test
    @DisplayName("OK: service is created with rush")
    public void withValidSubmissionServiceIsCreatedRush() {

        FilingPackage filingPackage = TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList());
        filingPackage.setRush(createRush());

        SubmitResponse actual = sut.createSubmission(Submission
                        .builder()
                        .id(TestHelpers.CASE_1)
                        .transactionId(TestHelpers.CASE_1)
                        .universalId(UUID.randomUUID().toString())
                        .navigationUrls(TestHelpers.createDefaultNavigation())
                        .expiryDate(10)
                        .clientAppName(CLIENT_APP_NAME)
                        .filingPackage(filingPackage)
                        .create(),
                AccountDetails.builder()
                        .fileRolePresent(true)
                        .accountId(BigDecimal.ONE)
                        .cardRegistered(true)
                        .universalId(UUID.randomUUID().toString())
                        .clientId(BigDecimal.TEN)
                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                        .create(), true);
        assertEquals("bnVsbC9wYWNrYWdlcmV2aWV3LzExMDAwP3BhY2thZ2VObz0xMTAwMA==", actual.getPackageRef());
    }

    private RushProcessing createRush() {
        return RushProcessing.builder()
                .courtDate("2001-11-26T12:00:00Z")
                .supportingDocuments(
                        Arrays.asList(
                                Document.builder()
                                        .serverFileName(FILE_PDF)
                                        .name(FILE_PDF)
                                        .create(),
                                Document.builder()
                                        .serverFileName(FILE_1_PDF)
                                        .name(FILE_1_PDF)
                                        .create()
                        )
                )
                .create();
    }

}
