package ca.bc.gov.open.efilingdiligenclient.diligen.diligenServiceImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenServiceImpl;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003Data;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003DataFileDetails;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class GetDocumentDetailsTest {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String JWT = "IMMAJWT";
    private static final Object JSON_OBJECT = "{ \"garbage\":\"garbage\" }";

    DiligenServiceImpl sut;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @Mock
    DocumentsApi documentsApiMock;

    @BeforeAll
    public void beforeAll() throws ApiException {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setProjectIdentifier(1);
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        Mockito.when(documentsApiMock.getApiClient()).thenReturn(new ApiClient());

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.ONE.intValue()))).thenReturn(getMockData("PROCESSED"));

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.TEN.intValue()))).thenReturn(getMockData("NOT_PROCESSED"));

        Mockito.when(documentsApiMock.apiDocumentsFileIdDetailsGet(ArgumentMatchers.eq(BigDecimal.ZERO.intValue()))).thenThrow(new ApiException());

        sut = new DiligenServiceImpl(null, diligenProperties, diligenAuthServiceMock, null, documentsApiMock);

    }

    @Test
    @DisplayName("Ok: document was returned")
    public void withValidDocumentIdDocumentSubmissionReturned() {

        Object result = sut.getDocumentDetails(BigDecimal.ONE);

       assertEquals(JSON_OBJECT, result);

    }

    @Test
    @DisplayName("Error: API Exception thrown")
    public void withInvalidDocumentIoException() {

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.getDocumentDetails(BigDecimal.ZERO));

    }

    @Test
    @DisplayName("Error: Status was not processed")
    public void withValidDocumentDiligenFailed() {

        Assertions.assertThrows(DiligenDocumentException.class, () -> sut.getDocumentDetails(BigDecimal.TEN));

    }

    private InlineResponse2003 getMockData(String status) {

        InlineResponse2003 inlineResponse2003 = new InlineResponse2003();
        InlineResponse2003Data data = new InlineResponse2003Data();
        InlineResponse2003DataFileDetails fileDetails = new InlineResponse2003DataFileDetails();
        fileDetails.setExtractedDocument(JSON_OBJECT);
        fileDetails.setFileStatus(status);
        data.setFileDetails(fileDetails);
        inlineResponse2003.setData(data);

        return inlineResponse2003;

    }

}
