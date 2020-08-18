package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.DOCUMENT_REQUIRED;
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

    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Submission submission = Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_1), any())).thenReturn(Optional.of(submission));


        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock);
    }

    @Test
    @DisplayName("200")
    public void withValidParamtersReturnDocumentProperties() {
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(TestHelpers.TYPE);
        documentProperties.setName("test.txt");
        documentProperties.setIsAmendment(true);
        documentProperties.setIsSupremeCourtScheduling(true);
        updateDocumentRequest.addDocumentsItem(documentProperties);

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(updateDocumentRequest))).thenReturn(Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
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
        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

    @Test
    @DisplayName("500")
    public void withExceptionThrownFromSoapInternalServerError() {
        UpdateDocumentRequest errorDocumentRequest = new UpdateDocumentRequest();
        errorDocumentRequest.addDocumentsItem(new DocumentProperties());

        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(errorDocumentRequest))).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));

        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), errorDocumentRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }


    @Test
    @DisplayName("404")
    public void withNoSubmissionReturnNotFound() {
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.addDocumentsItem(new DocumentProperties());
        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}
