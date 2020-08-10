package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class updateDocumentsTest {
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

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        DocumentDetails documentDetails = new DocumentDetails(TestHelpers.DESCRIPTION, BigDecimal.TEN);

        Mockito.when(documentStoreMock.getDocumentDetails(any(), any(), any())).thenReturn(documentDetails);

        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, new SubmissionMapperImpl(), null, efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock, null);

    }

    @Test
    @DisplayName("OK: with valid request should return submission")
    public void withValidRequestShouldreturnSubmission() {
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(TestHelpers.TYPE);
        documentProperties.setName("test.txt");
        updateDocumentRequest.addDocumentsItem(documentProperties);

        Submission submission = Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), new ArrayList<Document>(TestHelpers.createDocumentList())))
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();
        Submission actual = sut.updateDocuments(submission, updateDocumentRequest);

        Assertions.assertEquals(2, actual.getFilingPackage().getDocuments().size());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(1).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(1).getStatutoryFeeAmount());
    }
}
