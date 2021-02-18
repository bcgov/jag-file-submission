package ca.bc.gov.open.jag.efilingreviewerapi.extract.diligenServiceImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import ca.bc.gov.open.jag.efilingreviewerapi.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.DiligenServiceImpl;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class postDocumentTest {
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";
    public static final String JWT = "IMMAJWT";
    DiligenServiceImpl sut;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setProjectIdentifier(1);
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        sut = new DiligenServiceImpl(restTemplateMock, diligenProperties, diligenAuthServiceMock);

    }

    @Test
    @DisplayName("Ok: document was submitted")
    public void withValidDocumentSuccessfulDocumentSubmission() {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test.pdf", MediaType.MULTIPART_FORM_DATA.getType(), "TEST".getBytes());

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.noContent().build());

        BigDecimal result = sut.postDocument(DOCUMENT_TYPE, mockMultipartFile);

        assertEquals(BigDecimal.ONE, result);

    }

    @Test
    @DisplayName("Error: io exception")
    public void withInvalidDocumentIoException() throws IOException {

        MockMultipartFile mockMultipartFileException = Mockito.mock(MockMultipartFile.class);

        Mockito.when(mockMultipartFileException.getBytes()).thenThrow(IOException.class);

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.postDocument(DOCUMENT_TYPE, mockMultipartFileException));

    }

    @Test
    @DisplayName("Error: diligen failure")
    public void withValidDocumentDiligenFailed() {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test.pdf", MediaType.MULTIPART_FORM_DATA.getType(), "TEST".getBytes());

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.badRequest().build());

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.postDocument(DOCUMENT_TYPE, mockMultipartFile));

    }
}
