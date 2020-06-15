package ca.bc.gov.open.jag.efilingbackenddemo;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentService Test Suite")
public class DocumentServiceTest {

    private DocumentService sut;

    @Mock
    private ResourceLoader resourceLoaderMock;

    @BeforeAll
    public void setup() {

        MockitoAnnotations.initMocks(this);


        Resource resource = new ByteArrayResource("content".getBytes());
        Mockito.when(resourceLoaderMock.getResource(Mockito.eq("classpath:dummy.pdf"))).thenReturn(resource);
        sut = new DocumentService(resourceLoaderMock);

    }

    @Test
    @DisplayName("CASE 1: the ")
    public void getDocumentShouldReturnResource() throws IOException {

        ResponseEntity<Resource> actual = sut.getDocumentById("random");

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(7, actual.getBody().contentLength());

    }


}
