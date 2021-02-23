package ca.bc.gov.open.efilingdiligenclient.diligen.diligenServiceImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenServiceImpl;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.ProjectsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2002;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenServiceImpl test suite")
public class PostDocumentTest {
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";
    private static final String JWT = "IMMAJWT";
    private static final String DILIGEN_RESPONSE = "{\n" +
            "    \"data\": {\n" +
            "        \"documents\": [\n" +
            "            {\n" +
            "                \"file_id\": 810,\n" +
            "                \"file_tags\": [],\n" +
            "                \"duplicates_count\": 0,\n" +
            "                \"duplicate_paths\": null,\n" +
            "                \"file_name\": \"test-document.pdf\",\n" +
            "                \"added_on\": \"2021-02-17T22:04:39.000Z\",\n" +
            "                \"file_status\": \"PROCESSED\",\n" +
            "                \"out_of_scope\": null,\n" +
            "                \"ml_json\": {\n" +
            "                    \"data\": {\n" +
            "                        \"contract_date\": [\n" +
            "                            {\n" +
            "                                \"text_normalized\": \"0-0-0\",\n" +
            "                                \"text\": \"\"\n" +
            "                            }\n" +
            "                        ],\n" +
            "                        \"contract_name\": [\n" +
            "                            {\n" +
            "                                \"text_normalized\": \"\",\n" +
            "                                \"text\": \"\"\n" +
            "                            }\n" +
            "                        ],\n" +
            "                        \"document_type\": [\n" +
            "                            {\n" +
            "                                \"text\": \"other\"\n" +
            "                            }\n" +
            "                        ],\n" +
            "                        \"first_contract_page\": [\n" +
            "                            {\n" +
            "                                \"text\": \"-1\"\n" +
            "                            }\n" +
            "                        ],\n" +
            "                        \"contract_parties_page\": [\n" +
            "                            {\n" +
            "                                \"text\": \"[1]\"\n" +
            "                            }\n" +
            "                        ]\n" +
            "                    },\n" +
            "                    \"api_version\": \"2.2.0\"\n" +
            "                },\n" +
            "                \"number_of_pages\": 1,\n" +
            "                \"folder\": null,\n" +
            "                \"is_ocr\": false,\n" +
            "                \"is_converted\": false\n" +
            "            }\n" +
            "        ],\n" +
            "        \"reviews\": [],\n" +
            "        \"project_fields\": [\n" +
            "            {\n" +
            "                \"id\": 228,\n" +
            "                \"name\": \"001Answer: File Number\",\n" +
            "                \"project_id\": 2,\n" +
            "                \"created_by\": 3,\n" +
            "                \"field_type\": {\n" +
            "                    \"type\": \"TEXT_FIELD\",\n" +
            "                    \"multi\": false,\n" +
            "                    \"options\": []\n" +
            "                },\n" +
            "                \"data\": []\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"meta\": {\n" +
            "        \"row_count\": 1\n" +
            "    }\n" +
            "}";
    DiligenServiceImpl sut;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    DiligenAuthService diligenAuthServiceMock;

    @Mock
    ProjectsApi projectsApiMock;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        DiligenProperties diligenProperties = new DiligenProperties();
        diligenProperties.setBasePath("http:/test");
        diligenProperties.setProjectIdentifier(1);
        diligenProperties.setUsername(USERNAME);
        diligenProperties.setPassword(PASSWORD);

        Mockito.when(diligenAuthServiceMock.getDiligenJWT(any(), any())).thenReturn(JWT);

        sut = new DiligenServiceImpl(restTemplateMock, diligenProperties, diligenAuthServiceMock, projectsApiMock, new ObjectMapper());

    }

    @Test
    @DisplayName("Ok: document was submitted")
    public void withValidDocumentSuccessfulDocumentSubmission() throws ApiException, JsonProcessingException {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test.pdf", MediaType.MULTIPART_FORM_DATA.getType(), "TEST".getBytes());

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.noContent().build());

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Mockito.when(projectsApiMock.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(projectsApiMock.apiProjectsProjectIdDocumentsGet(any(), any(), any(), any())).thenReturn(objectMapper.readValue(DILIGEN_RESPONSE, InlineResponse2002.class));

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
