package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.DocumentRequiredException;
import ca.bc.gov.open.jag.efilingapi.error.DocumentTypeException;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
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
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @Mock
    private Jwt jwtMock;

    @BeforeEach
    public void setUp() throws IOException {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        Submission submission = TestHelpers.buildSubmission();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));


        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);

    }

    @Test
    @DisplayName("200")
    public void withValidParamtersReturnDocumentProperties() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType("AAB");
        documentProperties.setName("test.txt");
        documentProperties.setIsAmendment(true);
        documentProperties.setIsSupremeCourtScheduling(true);
        updateDocumentRequest.addDocumentsItem(documentProperties);

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(updateDocumentRequest), Mockito.any())).thenReturn(Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
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
    @DisplayName("400: with invalid parameters should throw DocumentRequiredException")
    public void withInValidParamtersShouldThrowDocumentRequiredException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), null));
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("500: with exception thrown from Soap should throw DocumentTypeException")
    public void withExceptionThrownFromSoapShouldThrowDocumentTypeException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        UpdateDocumentRequest errorDocumentRequest = new UpdateDocumentRequest();
        errorDocumentRequest.addDocumentsItem(new DocumentProperties());

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(errorDocumentRequest), Mockito.any())).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));

        DocumentTypeException exception = Assertions.assertThrows(DocumentTypeException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), errorDocumentRequest));
        Assertions.assertEquals(ErrorCode.DOCUMENT_TYPE_ERROR.toString(), exception.getErrorCode());
    }


    @Test
    @DisplayName("404")
    public void withNoSubmissionReturnNotFound() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.addDocumentsItem(new DocumentProperties());
        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }


    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.addDocumentsItem(new DocumentProperties());

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());

    }

}
