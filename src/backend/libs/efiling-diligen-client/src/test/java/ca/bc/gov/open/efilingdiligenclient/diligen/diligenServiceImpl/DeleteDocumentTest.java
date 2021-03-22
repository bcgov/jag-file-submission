package ca.bc.gov.open.efilingdiligenclient.diligen.diligenServiceImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenServiceImpl;
import ca.bc.gov.open.efilingdiligenclient.diligen.mapper.DiligenDocumentDetailsMapperImpl;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class DeleteDocumentTest {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    DiligenServiceImpl sut;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @Mock
    DocumentsApi documentsApiMock;

    @BeforeEach
    public void beforeEach() throws ApiException {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setProjectIdentifier(1);
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        Mockito.when(documentsApiMock.getApiClient()).thenReturn(new ApiClient());

        sut = new DiligenServiceImpl(null, diligenProperties, diligenAuthServiceMock, null, documentsApiMock, new DiligenDocumentDetailsMapperImpl());

    }

    @Test
    @DisplayName("Ok: document was deleted")
    public void whenValidRequestDeleteDocument() throws ApiException {

        Mockito.doNothing().when(documentsApiMock).apiDocumentsDelete(any());

        Assertions.assertDoesNotThrow(() -> sut.deleteDocument(BigDecimal.ONE));
        
    }

    @Test
    @DisplayName("Error: document was not deleted")
    public void whenInValidRequestDeleteDocument() throws ApiException {

        Mockito.doThrow(ApiException.class).when(documentsApiMock).apiDocumentsDelete(any());

        Assertions.assertThrows(DiligenDocumentException.class, () ->  sut.deleteDocument(BigDecimal.ONE));

    }

}
