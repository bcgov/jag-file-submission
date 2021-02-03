package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.DOCUMENT_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Upload Additional Submission Documents Test Suite")
public class UpdateDocumentPropertiesTest {

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private NavigationProperties navigationProperties;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private ClamAvService clamAvServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        Submission submission = TestHelpers.buildSubmission();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));


        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator);
    }

    @Test
    @DisplayName("200")
    public void withValidParamtersReturnDocumentProperties() {


        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        InitialDocument initialDocument = new InitialDocument();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(TestHelpers.TYPE);
        documentProperties.setName("test.txt");
        initialDocument.setDocumentProperties(documentProperties);
        initialDocument.setIsAmendment(true);
        initialDocument.setIsSupremeCourtScheduling(true);
        updateDocumentRequest.addDocumentsItem(initialDocument);

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(updateDocumentRequest), Mockito.any())).thenReturn(Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList()))
                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create());

        ResponseEntity<UpdateDocumentResponse> actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), updateDocumentRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().getDocuments().size());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getBody().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(true, actual.getBody().getDocuments().get(0).getIsSupremeCourtScheduling());
        Assertions.assertEquals(true, actual.getBody().getDocuments().get(0).getIsAmendment());
    }

    @Test
    @DisplayName("400")
    public void withInValidParamtersReturnBadRequest() {


        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

    @Test
    @DisplayName("500")
    public void withExceptionThrownFromSoapInternalServerError() {



        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UpdateDocumentRequest errorDocumentRequest = new UpdateDocumentRequest();
        errorDocumentRequest.addDocumentsItem(new InitialDocument());

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(errorDocumentRequest), Mockito.any())).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));

        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), errorDocumentRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }


    @Test
    @DisplayName("404")
    public void withNoSubmissionReturnNotFound() {


        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);


        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.addDocumentsItem(new InitialDocument());
        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }


    @Test
    @DisplayName("403: with no universal id is forbidden")
    public void withUserNotHavingUniversalIdShouldReturn403() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.addDocumentsItem(new InitialDocument());
        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest);

        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }
}
