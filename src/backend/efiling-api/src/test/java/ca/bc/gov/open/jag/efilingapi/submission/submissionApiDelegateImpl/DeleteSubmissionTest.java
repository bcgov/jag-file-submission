package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.TestHelpers.createDocumentList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteSubmissionTest {

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private AccountService accountServiceMock;


    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        Mockito.doNothing().when(documentStoreMock).evict(Mockito.any());
        Mockito.doNothing().when(submissionStoreMock).evict(Mockito.any(), Mockito.any());

        Submission submission = Submission
                .builder()
                .universalId(UUID.randomUUID())
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentList(), TestHelpers.createPartyList()))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_1), Mockito.any())).thenReturn(Optional.of(submission));


        NavigationProperties navigationProperties = new NavigationProperties();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, null, filingPackageMapper);

    }

    @Test
    @DisplayName("200: should delete from submission")
    public void withSubmissionIdAndTransactionIdShouldDeleteSubmission() {

        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_1, UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: without submission should return not found")
    public void withoutSubmissionShouldDeleteSubmission() {

        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_2, UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }


}
