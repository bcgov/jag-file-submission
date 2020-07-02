package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;


import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
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

    private static final String MIDDLE_NAME = "case1_middleName";
    private static final String LAST_NAME = "case1_lastName";
    private static final String FIRST_NAME = "case1_firstName";
    private static final String EMAIL = "case1_email";
    private static final DocumentProperties CASE1_DOCUMENT_PROPERTIES = TestHelpers.createDocumentProperties("header", "http://doc", "subtype", "case1_type");


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
    public void setUp() {

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
    public void withValidAccountShouldReturnSubmission() {

        GenerateUrlRequest request = new GenerateUrlRequest();
        request.setUserId(TestHelpers.CASE_1);
        request.setNavigation(TestHelpers.createDefaultNavigation());
        request.setDocumentProperties(CASE1_DOCUMENT_PROPERTIES);

        Submission actual = sut.generateFromRequest(request);

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
    public void withEmptySubmissionShouldThrowStoreException() {

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

    private void configureCase1(Fee fee) {


        AccountDetails accountDetails = getAccountDetails(true, TestHelpers.CASE_1.toString());

        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(
                        Mockito.eq(TestHelpers.CASE_1.toString().replace("-", "").toUpperCase()), Mockito.any()))
                .thenReturn(accountDetails);

        Submission submissionCase1 = Submission
                .builder()
                .accountDetails(accountDetails)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .documentProperties(CASE1_DOCUMENT_PROPERTIES)
                .fee(fee)
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
                        Mockito.eq(TestHelpers.CASE_2.toString().replace("-", "").toUpperCase()),
                        Mockito.any()))
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
                        Mockito.eq(TestHelpers.CASE_3.toString().replace("-", "").toUpperCase()),
                        Mockito.any()))
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
