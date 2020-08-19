package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

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

        Mockito.doNothing().when(submissionStoreMock).evict(Mockito.any(), Mockito.any());

        NavigationProperties navigationProperties = new NavigationProperties();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock);

    }

    @Test
    @DisplayName("200: should delete from submission")
    public void withSubmissionIdAndTransactionIdShouldDeleteSubmission() {

        ResponseEntity actual = sut.deleteSubmission(UUID.randomUUID(), UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

}
