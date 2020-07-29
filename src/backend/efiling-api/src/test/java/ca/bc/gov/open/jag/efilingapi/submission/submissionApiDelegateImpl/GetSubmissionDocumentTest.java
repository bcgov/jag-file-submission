package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetSubmissionDocumentTest {

    public static final String CONTENT = "content";
    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

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

        Mockito.when(documentStoreMock.get(Mockito.endsWith("test.txt"))).thenReturn(CONTENT.getBytes());

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock);

    }


    @Test
    @DisplayName("200: when document in cache should return 200")
    public void withDocumentInCacheShouldReturn200() {

        ResponseEntity<Resource> actual = sut.getSubmissionDocument(UUID.randomUUID(), UUID.randomUUID(), "test.txt");

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: when document is missing should retunr 404")
    public void withNoDocumentShouldReturn404() {
        ResponseEntity<Resource> actual = sut.getSubmissionDocument(UUID.randomUUID(), UUID.randomUUID(), "test2.txt");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

}
