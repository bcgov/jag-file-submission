package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static ca.bc.gov.open.jag.efilingapi.api.model.EndpointAccess.VerbEnum.POST;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class generateFromRequestTest {

    private static final String CASE_1_MIDDLE_NAME = "case1_middleName";
    private static final String CASE_1_LAST_NAME = "case1_lastName";
    private static final String CASE_1_FIRST_NAME = "case1_firstName";
    private static final String CASE_1_EMAIL = "case1_email";
    private static final DocumentProperties CASE1_DOCUMENT_PROPERTIES = TestHelpers.createDocumentProperties("header", "http://doc", "subtype", "case1_type");

    private static final String CASE_2_MIDDLE_NAME = "case2_middleName";
    private static final String CASE_2_LAST_NAME = "case2_lastName";
    private static final String CASE_2_FIRST_NAME = "case2_firstName";
    private static final String CASE_2_EMAIL = "case2_email";

    private static final String CASE_3_MIDDLE_NAME = "case2_middleName";
    private static final String CASE_3_LAST_NAME = "case2_lastName";
    private static final String CASE_3_FIRST_NAME = "case2_firstName";
    private static final String CASE_3_EMAIL = "case2_email";


    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;
    @Mock
    private CacheProperties cachePropertiesMock;
    @Mock
    private FeeService feeServiceMock;
    @Mock
    private EfilingAccountService efilingAccountServiceMock;


    @BeforeAll
    public void setUp() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        Fee fee = new Fee(BigDecimal.valueOf(7.00));
        Mockito.doReturn(fee)
                .when(feeServiceMock)
                .getFee(Mockito.any());

        configureCase1(fee);
        configureCase2();
        configureCase3();

        CacheProperties.Redis redis = new CacheProperties.Redis();
        redis.setTimeToLive(Duration.ofMillis(100));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redis);

        // Testing mapper as part of this unit test
        SubmissionMapper submissionMapper = new SubmissionMapperImpl();
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, submissionMapper, efilingAccountServiceMock, feeServiceMock);

    }



    @Test
    @DisplayName("OK: with valid account should retun submission")
    public void withValidAccountShouldReturnSubmission() throws NestedEjbException_Exception {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setUserId(TestHelpers.CASE_1);
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setDocumentProperties(CASE1_DOCUMENT_PROPERTIES);

        Submission actual = sut.generateFromRequest(request);

        Assertions.assertEquals(CASE_1_EMAIL, actual.getAccountDetails().getEmail());
        Assertions.assertEquals(CASE_1_FIRST_NAME, actual.getAccountDetails().getFirstName());
        Assertions.assertEquals(CASE_1_LAST_NAME, actual.getAccountDetails().getLastName());
        Assertions.assertEquals(CASE_1_MIDDLE_NAME, actual.getAccountDetails().getMiddleName());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getAccountId());
        Assertions.assertEquals(BigDecimal.ONE, actual.getAccountDetails().getClientId());
        Assertions.assertEquals(TestHelpers.ERROR_URL, actual.getNavigation().getError().getUrl());
        Assertions.assertEquals(TestHelpers.CANCEL_URL, actual.getNavigation().getCancel().getUrl());
        Assertions.assertEquals(TestHelpers.SUCCESS_URL, actual.getNavigation().getSuccess().getUrl());
        Assertions.assertEquals(10, actual.getExpiryDate());
        Assertions.assertEquals("subtype", actual.getDocumentProperties().getSubType());
        Assertions.assertEquals("case1_type", actual.getDocumentProperties().getType());
        Assertions.assertEquals("http://doc", actual.getDocumentProperties().getSubmissionAccess().getUrl());
        Assertions.assertEquals("header", actual.getDocumentProperties().getSubmissionAccess().getHeaders().get("header"));
        Assertions.assertEquals(POST, actual.getDocumentProperties().getSubmissionAccess().getVerb());
        Assertions.assertEquals(BigDecimal.valueOf(7.0), actual.getFee().getAmount());
        Assertions.assertNotNull(actual.getId());

    }

    @Test
    @DisplayName("Exception: with empty submission from store should throw StoreException")
    public void withEmptySubmissionShouldThrowStoreException() throws NestedEjbException_Exception {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setUserId(TestHelpers.CASE_2);
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setDocumentProperties(CASE1_DOCUMENT_PROPERTIES);

        Assertions.assertThrows(StoreException.class, () -> sut.generateFromRequest(request));

    }

    @Test
    @DisplayName("Exception: with no file role should throw InvalidAccountStateException")
    public void withNoFileRoleShouldThrowInvalidAccountStateException() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setUserId(TestHelpers.CASE_3);
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setDocumentProperties(CASE1_DOCUMENT_PROPERTIES);

        Assertions.assertThrows(InvalidAccountStateException.class, () -> sut.generateFromRequest(request));

    }

    private void configureCase1(Fee fee) throws NestedEjbException_Exception {
        AccountDetails accountDetailsCase1 = AccountDetails
                .builder()
                .fileRolePresent(true)
                .middleName(CASE_1_MIDDLE_NAME)
                .lastName(CASE_1_LAST_NAME)
                .firstName(CASE_1_FIRST_NAME)
                .email(CASE_1_EMAIL)
                .accountId(BigDecimal.TEN)
                .clientId(BigDecimal.ONE)
                .create();

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_1.toString().replace("-", "").toUpperCase())))
                .thenReturn(accountDetailsCase1);

        Submission submissionCase1 = Submission
                .builder()
                .accountDetails(accountDetailsCase1)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .documentProperties(CASE1_DOCUMENT_PROPERTIES)
                .fee(fee)
                .create();

        Mockito
                .doReturn(Optional.of(submissionCase1))
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> StringUtils.equals(CASE_1_FIRST_NAME, x.getAccountDetails().getFirstName())));
    }

    private void configureCase2() throws NestedEjbException_Exception {
        AccountDetails accountDetailsCase2 = AccountDetails
                .builder()
                .fileRolePresent(true)
                .middleName(CASE_2_MIDDLE_NAME)
                .lastName(CASE_2_LAST_NAME)
                .firstName(CASE_2_FIRST_NAME)
                .email(CASE_2_EMAIL)
                .accountId(BigDecimal.TEN)
                .clientId(BigDecimal.ONE)
                .create();

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_2.toString().replace("-", "").toUpperCase())))
                .thenReturn(accountDetailsCase2);

        Mockito
                .doReturn(Optional.empty())
                .when(submissionStoreMock).put(
                ArgumentMatchers.argThat(x -> StringUtils.equals(CASE_2_FIRST_NAME, x.getAccountDetails().getFirstName())));
    }

    private void configureCase3() throws NestedEjbException_Exception {
        AccountDetails accountDetails = AccountDetails
                .builder()
                .fileRolePresent(false)
                .middleName(CASE_3_MIDDLE_NAME)
                .lastName(CASE_3_LAST_NAME)
                .firstName(CASE_3_FIRST_NAME)
                .email(CASE_3_EMAIL)
                .accountId(BigDecimal.TEN)
                .clientId(BigDecimal.ONE)
                .create();

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_3.toString().replace("-", "").toUpperCase())))
                .thenReturn(accountDetails);

    }


}
