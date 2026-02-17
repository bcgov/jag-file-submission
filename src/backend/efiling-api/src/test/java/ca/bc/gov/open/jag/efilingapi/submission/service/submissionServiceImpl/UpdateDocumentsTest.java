package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateDocumentsTest {
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

        MockitoAnnotations.openMocks(this);

        DocumentTypeDetails documentDetails = new DocumentTypeDetails(TestHelpers.DESCRIPTION, "TYPE",BigDecimal.TEN, false, false,true);

        Mockito.when(documentStoreMock.getDocumentDetails(any(), any(), any())).thenReturn(documentDetails);

        NavigationProperties navigationProperties = new NavigationProperties();

        sut = new SubmissionServiceImpl(
                submissionStoreMock,
                cachePropertiesMock,
                new SubmissionMapperImpl(),
                new PartyMapperImpl(),
                efilingLookupService,
                efilingCourtService,
                efilingSubmissionServiceMock,
                null,
                documentStoreMock,
                null,
                null, navigationProperties);

    }

    @Test
    @DisplayName("OK: with valid request should return submission")
    public void withValidRequestShouldreturnSubmission() {
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        DocumentProperties initialDocument = new DocumentProperties();
        initialDocument.setType("AAB");
        initialDocument.setName("test.txt");
        updateDocumentRequest.addDocumentsItem(initialDocument);

        Submission submission = TestHelpers.buildSubmission();
        Submission actual = sut.updateDocuments(submission, updateDocumentRequest, new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()));

        Assertions.assertEquals(2, actual.getFilingPackage().getDocuments().size());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD, actual.getFilingPackage().getDocuments().get(0).getSubType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(1).getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFilingPackage().getDocuments().get(1).getStatutoryFeeAmount());
        Assertions.assertEquals(SubmissionConstants.SUBMISSION_ODOC_DOCUMENT_SUB_TYPE_CD, actual.getFilingPackage().getDocuments().get(1).getSubType());

    }
}
