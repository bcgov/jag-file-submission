package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;


import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class generateFromRequestTest {

    private static final String MIDDLE_NAME = "case1_middleName";
    private static final String LAST_NAME = "case1_lastName";
    private static final String FIRST_NAME = "case1_firstName";
    private static final String EMAIL = "case1_email";

    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;

    @Mock
    private EfilingLookupService efilingLookupService;

    @Mock
    private EfilingCourtService efilingCourtService;

    @Mock
    private DocumentStore documentStoreMock;

    @BeforeAll
    public void setUp() throws DatatypeConfigurationException {

        MockitoAnnotations.initMocks(this);

        ServiceFees fee = new ServiceFees( BigDecimal.valueOf(7.00), "DCFL");
        Mockito.doReturn(fee)
                .when(efilingLookupService)
                .getServiceFee(any());

        Mockito.when(efilingCourtService.getCourtDescription(any())).thenReturn(new CourtDetails(BigDecimal.TEN, TestHelpers.COURT_DESCRIPTION));

        configureCase1(fee);
        configureCase2();
        configureCase3();

        CacheProperties.Redis redis = new CacheProperties.Redis();
        redis.setTimeToLive(Duration.ofMillis(100));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redis);

        // Testing mapper as part of this unit test
        SubmissionMapper submissionMapper = new SubmissionMapperImpl();
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, submissionMapper, efilingAccountServiceMock, efilingLookupService, efilingCourtService, documentStoreMock);

    }


    @Test
    @DisplayName("OK: with valid account should return submission")
    public void withValidAccountShouldReturnSubmission() {
        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientApplication(TestHelpers.createClientApplication("app", "app"));
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createCourt(), TestHelpers.createDocumentPropertiesList()));

        Submission actual = sut.generateFromRequest(TestHelpers.CASE_1, TestHelpers.CASE_1, request);

        Assertions.assertEquals(TestHelpers.CASE_1.toString() + EMAIL, actual.getAccountDetails().getEmail());
        Assertions.assertEquals(TestHelpers.CASE_1.toString() + FIRST_NAME, actual.getAccountDetails().getFirstName());
        Assertions.assertEquals(TestHelpers.CASE_1.toString() + LAST_NAME, actual.getAccountDetails().getLastName());
        Assertions.assertEquals(TestHelpers.CASE_1.toString() + MIDDLE_NAME, actual.getAccountDetails().getMiddleName());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getAccountId());
        Assertions.assertEquals(BigDecimal.ONE, actual.getAccountDetails().getClientId());
        Assertions.assertEquals(TestHelpers.ERROR_URL, actual.getNavigation().getError().getUrl());
        Assertions.assertEquals(TestHelpers.CANCEL_URL, actual.getNavigation().getCancel().getUrl());
        Assertions.assertEquals(TestHelpers.SUCCESS_URL, actual.getNavigation().getSuccess().getUrl());
        Assertions.assertEquals(10, actual.getExpiryDate());
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getFilingPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getFilingPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getFilingPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getFilingPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getFilingPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getFilingPackage().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getFilingPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(TestHelpers.COURT_DESCRIPTION, actual.getFilingPackage().getCourt().getLocationDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getCourt().getLocationId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals("application/txt", actual.getFilingPackage().getDocuments().get(0).getMimeType());

    }

    @Test
    @DisplayName("Exception: with empty submission from store should throw StoreException")
    public void withEmptySubmissionShouldThrowStoreException() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientApplication(TestHelpers.createClientApplication("app", "type"));
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createCourt(), TestHelpers.createDocumentPropertiesList()));


        Assertions.assertThrows(StoreException.class, () -> sut.generateFromRequest(TestHelpers.CASE_2, TestHelpers.CASE_2, request));

    }

    @Test
    @DisplayName("Exception: with no file role should throw InvalidAccountStateException")
    public void withNoFileRoleShouldThrowInvalidAccountStateException() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientApplication(TestHelpers.createClientApplication("app", "type"));
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createCourt(), TestHelpers.createDocumentPropertiesList()));


        Assertions.assertThrows(InvalidAccountStateException.class, () -> sut.generateFromRequest(TestHelpers.CASE_3, TestHelpers.CASE_3, request));

    }

    @Test
    @DisplayName("TEMP: test demo acount")
    public void testDemo()  {

        UUID fakeaccount = UUID.fromString("88da92db-0791-491e-8c58-1a969e67d2fb");
        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setClientApplication(TestHelpers.createClientApplication("app", "type"));
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setFilingPackage(TestHelpers.createInitalPackage(TestHelpers.createCourt(), TestHelpers.createDocumentPropertiesList()));

        AccountDetails accountDetails =  AccountDetails.builder().lastName("lastName").create();

        ServiceFees fee = new ServiceFees( BigDecimal.valueOf(10), "DCFL");
        List<ServiceFees> fees = new ArrayList<>();


        Submission submissionCase1 = Submission
                .builder()
                .accountDetails(accountDetails)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .create();

        Mockito
                .doReturn(Optional.of(submissionCase1))
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> StringUtils.equals("Ross", x.getAccountDetails().getLastName())));

        Submission actual = sut.generateFromRequest(fakeaccount, fakeaccount, request);

        Assertions.assertEquals("lastName", actual.getAccountDetails().getLastName());


    }

    private void configureCase1(ServiceFees fee) {


        AccountDetails accountDetails = getAccountDetails(true, TestHelpers.CASE_1.toString());

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_1),
                        any()))
                .thenReturn(accountDetails);

        Mockito.when(documentStoreMock.getDocumentDetails(any(), any(), any()))
                .thenReturn(new DocumentDetails(TestHelpers.DESCRIPTION, BigDecimal.TEN));

        Submission submissionCase1 = Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .owner(TestHelpers.CASE_1)
                .accountDetails(accountDetails)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .create();

        Mockito
                .doReturn(Optional.of(submissionCase1))
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> StringUtils.equals(TestHelpers.CASE_1.toString() + FIRST_NAME, x.getAccountDetails().getFirstName())));
    }

    private void configureCase2() {


        AccountDetails accountDetails = getAccountDetails(true, TestHelpers.CASE_2.toString());

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_2),
                        any()))
                .thenReturn(accountDetails);

        Mockito
                .doReturn(Optional.empty())
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> StringUtils.equals(TestHelpers.CASE_2.toString() + FIRST_NAME, x.getAccountDetails().getFirstName())));
    }

    private void configureCase3() {

        AccountDetails accountDetails = getAccountDetails(false, TestHelpers.CASE_3.toString());

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_3),
                        any()))
                .thenReturn(accountDetails);

    }

    private AccountDetails getAccountDetails(boolean fileRolePresent, String _case) {
        return AccountDetails
                .builder()
                .fileRolePresent(fileRolePresent)
                .middleName(_case + MIDDLE_NAME)
                .lastName(_case + LAST_NAME)
                .firstName(_case + FIRST_NAME)
                .email(_case + EMAIL)
                .accountId(BigDecimal.TEN)
                .clientId(BigDecimal.ONE)
                .create();
    }

}
