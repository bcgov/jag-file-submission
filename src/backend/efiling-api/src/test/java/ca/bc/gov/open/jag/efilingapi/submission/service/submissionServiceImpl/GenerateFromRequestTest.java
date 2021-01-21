package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;


import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Party;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenerateFromRequestTest {

    public static final String CLIENT_APP_NAME = "clientAppName";
    public static final String APP_CODE = "CODE";

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

    @BeforeAll
    public void beforeAll() throws DatatypeConfigurationException {

        MockitoAnnotations.openMocks(this);

        ServiceFees fee = new ServiceFees( BigDecimal.valueOf(7.00), "DCFL");
        Mockito.doReturn(fee)
                .when(efilingLookupService)
                .getServiceFee(any());

        Mockito.when(efilingCourtService.getCourtDescription(any(), any(), any())).thenReturn(new CourtDetails(BigDecimal.TEN, TestHelpers.COURT_DESCRIPTION, TestHelpers.CLASS_DESCRIPTION, TestHelpers.LEVEL_DESCRIPTION));

        configureCase1(fee);
        configureCase2();
        configureCase3();

        CacheProperties.Redis redis = new CacheProperties.Redis();
        redis.setTimeToLive(Duration.ofMillis(100));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redis);

        // Testing mapper as part of this unit test
        SubmissionMapper submissionMapper = new SubmissionMapperImpl();
        NavigationProperties navigationProperties = new NavigationProperties();
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, submissionMapper, new PartyMapperImpl(), efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock, null, null, navigationProperties);

    }


    @Test
    @DisplayName("OK: with valid account with court file number and no parties should return submission")
    public void withValidAccountWithCourtFileNumberAndNoPartiesShouldReturnSubmission() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientAppName(CLIENT_APP_NAME);
        request.setNavigationUrls(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createApiCourt(), TestHelpers.createDocumentPropertiesList()));

        Mockito.when(efilingCourtService.checkValidLevelClassLocation(any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingCourtService.checkValidCourtFileNumber(any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingDocumentService.getDocumentTypes(any(), any())).thenReturn(TestHelpers.createValidDocumentTypesList());

        Submission actual = sut.generateFromRequest(APP_CODE, new SubmissionKey(TestHelpers.CASE_1_STRING, TestHelpers.CASE_1, TestHelpers.CASE_1), request);

        Assertions.assertEquals(TestHelpers.ERROR_URL, actual.getNavigationUrls().getError());
        Assertions.assertEquals(TestHelpers.CANCEL_URL, actual.getNavigationUrls().getCancel());
        Assertions.assertEquals(TestHelpers.SUCCESS_URL, actual.getNavigationUrls().getSuccess());
        Assertions.assertEquals(10, actual.getExpiryDate());
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getFilingPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getFilingPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getFilingPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getFilingPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getFilingPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getFilingPackage().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE.getValue(), actual.getFilingPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getCourt().getAgencyId());
        Assertions.assertEquals(TestHelpers.COURT_DESCRIPTION, actual.getFilingPackage().getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, actual.getFilingPackage().getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, actual.getFilingPackage().getCourt().getClassDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD, actual.getFilingPackage().getDocuments().get(0).getSubType());
        Assertions.assertEquals("application/txt", actual.getFilingPackage().getDocuments().get(0).getMimeType());
        Assertions.assertEquals(2, actual.getFilingPackage().getParties().size());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(0).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(0).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(0).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(0).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(0).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(0).getRoleTypeCd());

        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(1).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(1).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(1).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(1).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(1).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(1).getRoleTypeCd());


    }

    @Test
    @DisplayName("OK: with valid account with no court file number and valid parties should return submission")
    public void withValidAccountWithNoCourtFileNumberAndValidPartiesShouldReturnSubmission() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientAppName(CLIENT_APP_NAME);
        request.setNavigationUrls(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createApiCourt(), TestHelpers.createDocumentPropertiesList()));
        request.getFilingPackage().getCourt().setFileNumber("");

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.ABC);
        parties.add(party);
        request.getFilingPackage().setParties(parties);

        Mockito.when(efilingCourtService.checkValidLevelClassLocation(any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingLookupService.getValidPartyRoles(any(), any(), any())).thenReturn(TestHelpers.createValidPartyRoles());
        Mockito.when(efilingDocumentService.getDocumentTypes(any(), any())).thenReturn(TestHelpers.createValidDocumentTypesList());

        Submission actual = sut.generateFromRequest(APP_CODE, new SubmissionKey(TestHelpers.CASE_1_STRING, TestHelpers.CASE_1, TestHelpers.CASE_1), request);

        Assertions.assertEquals(TestHelpers.ERROR_URL, actual.getNavigationUrls().getError());
        Assertions.assertEquals(TestHelpers.CANCEL_URL, actual.getNavigationUrls().getCancel());
        Assertions.assertEquals(TestHelpers.SUCCESS_URL, actual.getNavigationUrls().getSuccess());
        Assertions.assertEquals(10, actual.getExpiryDate());
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getFilingPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getFilingPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getFilingPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getFilingPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getFilingPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getFilingPackage().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE.getValue(), actual.getFilingPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getCourt().getAgencyId());
        Assertions.assertEquals(TestHelpers.COURT_DESCRIPTION, actual.getFilingPackage().getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, actual.getFilingPackage().getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, actual.getFilingPackage().getCourt().getClassDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD, actual.getFilingPackage().getDocuments().get(0).getSubType());
        Assertions.assertEquals("application/txt", actual.getFilingPackage().getDocuments().get(0).getMimeType());
        Assertions.assertEquals(2, actual.getFilingPackage().getParties().size());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(0).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(0).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(0).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(0).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(0).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(0).getRoleTypeCd());

        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(1).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(1).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(1).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(1).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(1).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(1).getRoleTypeCd());

    }

    @Test
    @DisplayName("OK: with valid account no rushed should return submission")
    public void withValidAccountNoRushedShouldReturnSubmission() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientAppName(CLIENT_APP_NAME);
        request.setNavigationUrls(TestHelpers.createDefaultNavigation());

        InitialPackage filingPackage = TestHelpers.createInitalPackage(TestHelpers.createApiCourt(), TestHelpers.createDocumentPropertiesList());
        filingPackage.getCourt().setLevel("TEST2");
        request.setFilingPackage(filingPackage);

        Mockito.when(efilingCourtService.checkValidLevelClassLocation(any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingCourtService.checkValidCourtFileNumber(any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingDocumentService.getDocumentTypes(any(), any())).thenReturn(TestHelpers.createValidDocumentTypesList());

        Submission actual = sut.generateFromRequest(APP_CODE, new SubmissionKey(TestHelpers.CASE_1_STRING, TestHelpers.CASE_1, TestHelpers.CASE_1), request);

        Assertions.assertEquals(TestHelpers.ERROR_URL, actual.getNavigationUrls().getError());
        Assertions.assertEquals(TestHelpers.CANCEL_URL, actual.getNavigationUrls().getCancel());
        Assertions.assertEquals(TestHelpers.SUCCESS_URL, actual.getNavigationUrls().getSuccess());
        Assertions.assertEquals(10, actual.getExpiryDate());
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getFilingPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getFilingPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getFilingPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getFilingPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getFilingPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getFilingPackage().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE.getValue(), actual.getFilingPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getCourt().getAgencyId());
        Assertions.assertEquals(TestHelpers.COURT_DESCRIPTION, actual.getFilingPackage().getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, actual.getFilingPackage().getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, actual.getFilingPackage().getCourt().getClassDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD, actual.getFilingPackage().getDocuments().get(0).getSubType());
        Assertions.assertEquals("application/txt", actual.getFilingPackage().getDocuments().get(0).getMimeType());
        Assertions.assertEquals(2, actual.getFilingPackage().getParties().size());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(0).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(0).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(0).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(0).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(0).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(0).getRoleTypeCd());

        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.getFilingPackage().getParties().get(1).getFirstName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.getFilingPackage().getParties().get(1).getMiddleName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.getFilingPackage().getParties().get(1).getLastName());
        Assertions.assertEquals(TestHelpers.NAME_TYPE_CD, actual.getFilingPackage().getParties().get(1).getNameTypeCd());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_CD, actual.getFilingPackage().getParties().get(1).getPartyTypeCd());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_CD, actual.getFilingPackage().getParties().get(1).getRoleTypeCd());

    }

    @Test
    @DisplayName("Exception: with empty submission from store should throw StoreException")
    public void withEmptySubmissionShouldThrowStoreException() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientAppName(CLIENT_APP_NAME);
        request.setNavigationUrls(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createApiCourt(), TestHelpers.createDocumentPropertiesList()));

        Mockito.when(efilingCourtService.checkValidLevelClassLocation(any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingCourtService.checkValidCourtFileNumber(any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(efilingDocumentService.getDocumentTypes(any(), any())).thenReturn(TestHelpers.createValidDocumentTypesList());

        Assertions.assertThrows(StoreException.class, () -> sut.generateFromRequest(APP_CODE, new SubmissionKey(TestHelpers.CASE_2_STRING, TestHelpers.CASE_2, TestHelpers.CASE_2), request));
    }

    private void configureCase1(ServiceFees fee) {

        AccountDetails accountDetails = getAccountDetails(true, TestHelpers.CASE_1.toString());

        Mockito.when(documentStoreMock.getDocumentDetails(Mockito.eq("LEVEL"), any(), any()))
                .thenReturn(new DocumentDetails(TestHelpers.DESCRIPTION, BigDecimal.TEN, true, true));

        Mockito.when(documentStoreMock.getDocumentDetails(Mockito.eq("TEST2"), any(), any()))
                .thenReturn(new DocumentDetails(TestHelpers.DESCRIPTION, BigDecimal.TEN, true, false));

        Submission submissionCase1 = Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .transactionId(TestHelpers.CASE_1)
                .navigationUrls(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList()))
                .create();

        Mockito
                .doReturn(Optional.of(submissionCase1))
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> x.getTransactionId() == TestHelpers.CASE_1));
    }

    private void configureCase2() {

        AccountDetails accountDetails = getAccountDetails(true, TestHelpers.CASE_2.toString());

        Mockito
                .doReturn(Optional.empty())
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> x.getTransactionId() == TestHelpers.CASE_2));
    }

    private void configureCase3() {

        AccountDetails accountDetails = getAccountDetails(false, TestHelpers.CASE_3.toString());

    }

    private AccountDetails getAccountDetails(boolean fileRolePresent, String _case) {
        return AccountDetails
                .builder()
                .fileRolePresent(fileRolePresent)
                .accountId(BigDecimal.TEN)
                .clientId(BigDecimal.ONE)
                .create();
    }

}
