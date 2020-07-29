package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.Account;
import ca.bc.gov.open.jag.efilingapi.api.model.GetSubmissionResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingSubmissionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetSubmissionTest {

    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String SERVICE_TYPE_CD = "DCFL";
    private static final String SERVICE_TYPE_CD1 = "NOTDCFL";

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private DocumentStore documentStoreMock;


    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");
        Submission submissionWithCsoAccount = Submission
                .builder()
                .accountDetails(
                        AccountDetails
                                .builder()
                                .universalId(TestHelpers.CASE_2)
                                .accountId(BigDecimal.TEN)
                                .firstName(FIRST_NAME + TestHelpers.CASE_2)
                                .lastName(LAST_NAME + TestHelpers.CASE_2)
                                .middleName(MIDDLE_NAME + TestHelpers.CASE_2)
                                .fileRolePresent(true)
                                .email(EMAIL + TestHelpers.CASE_2).create())
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_2), Mockito.any())).thenReturn(Optional.of(submissionWithCsoAccount));

        Submission submissionWithoutCsoAccount = Submission
                .builder()
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_3), Mockito.any())).thenReturn(Optional.of(submissionWithoutCsoAccount));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock);

    }

    @Test
    @DisplayName("404: With null redis storage response return NotFound")
    public void withNullRedisStorageResponseReturnNotFound() {
        ResponseEntity<GetSubmissionResponse> actual = sut.getSubmission(UUID.randomUUID(), TestHelpers.CASE_1);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    @DisplayName("200: With user having cso account and efiling role return submission details")
    public void withUserHavingCsoAccountShouldReturnUserDetailsAndAccount() {

        ResponseEntity<GetSubmissionResponse> actual = sut.getSubmission(UUID.randomUUID(), TestHelpers.CASE_2);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(TestHelpers.CASE_2, actual.getBody().getUserDetails().getUniversalId());
        assertEquals(EMAIL + TestHelpers.CASE_2, actual.getBody().getUserDetails().getEmail());
        assertEquals(FIRST_NAME + TestHelpers.CASE_2, actual.getBody().getUserDetails().getFirstName());
        assertEquals(LAST_NAME + TestHelpers.CASE_2, actual.getBody().getUserDetails().getLastName());
        assertEquals(MIDDLE_NAME + TestHelpers.CASE_2, actual.getBody().getUserDetails().getMiddleName());
        assertEquals(1, actual.getBody().getUserDetails().getAccounts().size());
        assertEquals(Account.TypeEnum.CSO, actual.getBody().getUserDetails().getAccounts().stream().findFirst().get().getType());
        assertEquals("10", actual.getBody().getUserDetails().getAccounts().stream().findFirst().get().getIdentifier());
        assertEquals(TestHelpers.SUCCESS_URL, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(TestHelpers.CANCEL_URL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(TestHelpers.ERROR_URL, actual.getBody().getNavigation().getError().getUrl());

    }

    @Test
    @DisplayName("200: With user not having cso account")
    public void withUserHavingNoCsoAccountShouldReturnUserDetailsButNoAccount() {

        ResponseEntity<GetSubmissionResponse> actual = sut.getSubmission(UUID.randomUUID(), TestHelpers.CASE_3);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(EMAIL, actual.getBody().getUserDetails().getEmail());
        assertEquals(FIRST_NAME, actual.getBody().getUserDetails().getFirstName());
        assertEquals(LAST_NAME, actual.getBody().getUserDetails().getLastName());
        assertEquals(MIDDLE_NAME, actual.getBody().getUserDetails().getMiddleName());
        assertNull(actual.getBody().getUserDetails().getAccounts());
        assertEquals(TestHelpers.SUCCESS_URL, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(TestHelpers.CANCEL_URL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(TestHelpers.ERROR_URL, actual.getBody().getNavigation().getError().getUrl());
    }

    private List<ServiceFees> createFees() {
        ServiceFees fee1 = new ServiceFees(BigDecimal.TEN, SERVICE_TYPE_CD);
        ServiceFees fee2 = new ServiceFees(BigDecimal.ONE, SERVICE_TYPE_CD1);
        return Arrays.asList(fee1, fee2);
    }

}
