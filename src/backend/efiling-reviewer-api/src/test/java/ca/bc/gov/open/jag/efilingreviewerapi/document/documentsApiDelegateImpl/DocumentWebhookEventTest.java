package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponseData;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentWebhookEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentWebhookEventData;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.Extract;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.WebHookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentWebhookEventTest {


    private DocumentsApiDelegateImpl sut;

    @Mock
    private DiligenService diligenServiceMock;

    @Mock
    private ExtractStore extractStoreMock;

    @Mock
    private DocumentValidator documentValidatorMock;

    @Mock
    private FieldProcessor fieldProcessorMock;

    @Mock
    private StringRedisTemplate stringRedisTemplateMock;

    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @Mock
    private WebHookService webHookServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl();

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode result = objectMapper.createObjectNode();
        result.put("test", "test");

        Mockito.when(fieldProcessorMock.getJson(Mockito.any(), Mockito.any())).thenReturn(result);

        Mockito.when(diligenServiceMock.getDocumentDetails(Mockito.any())).thenReturn(DiligenDocumentDetails.builder().create());

        DocumentTypeConfiguration configuration = DocumentTypeConfiguration.builder().create();
        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType(Mockito.eq("TYPE"))).thenReturn(configuration);

        ProjectFieldsResponse projectFieldResponse = new ProjectFieldsResponse();
        ProjectFieldsResponseData data = new ProjectFieldsResponseData();
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        List<String> values = new ArrayList<>();
        values.add("test");
        field.setValues(values);
        fields.add(field);
        data.setFields(fields);
        projectFieldResponse.setData(data);
        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(DiligenDocumentDetails.builder()
                .projectFieldsResponse(projectFieldResponse).create());
        Mockito.when(extractStoreMock.get(Mockito.eq(BigDecimal.ONE))).thenReturn(Optional.of(ExtractRequest.builder()
                .document(Document.builder()
                        .type("TYPE")
                        .create())
                .extract(Extract.builder()
                        .id(UUID.randomUUID())
                        .transactionId(UUID.randomUUID())
                        .useWebhook(true)
                        .create())
                .create()));

        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.ZERO))).thenReturn(DiligenDocumentDetails.builder()
                .projectFieldsResponse(projectFieldResponse).create());

        Mockito.when(extractStoreMock.get(Mockito.eq(BigDecimal.ZERO))).thenReturn(Optional.of(ExtractRequest.builder()
                .document(Document.builder()
                        .type("TYPE")
                        .create())
                .extract(Extract.builder()
                        .id(UUID.randomUUID())
                        .transactionId(UUID.randomUUID())
                        .useWebhook(false)
                        .create())
                .create()));

        Mockito.when(extractStoreMock.get(Mockito.eq(BigDecimal.TEN))).thenReturn(Optional.of(ExtractRequest.builder()
                .document(Document.builder()
                        .type("NO-CONFIG")
                        .create())
                .create()));

        Mockito.doNothing().when(webHookServiceMock).sendDocumentReady(Mockito.any(), Mockito.any());

        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extractRequestMapper, extractStoreMock, stringRedisTemplateMock, fieldProcessorMock, documentValidatorMock, documentTypeConfigurationRepositoryMock, null, webHookServiceMock);

    }

    @Test
    @DisplayName("200: succeeds with correctly formatted request body")
    public void testSuccessEvent() {
        ArrayList<DocumentWebhookEventData> documentWebhookEventDataList = new ArrayList<DocumentWebhookEventData>();
        DocumentWebhookEvent documentWebhookEvent = new DocumentWebhookEvent();
        DocumentWebhookEventData documentWebhookEventData = new DocumentWebhookEventData();
        documentWebhookEventData.setId(BigDecimal.ONE);
        documentWebhookEventData.setStatus("Processed");
        documentWebhookEventDataList.add(documentWebhookEventData);
        documentWebhookEvent.setData(documentWebhookEventDataList);
        documentWebhookEvent.setEvent("FILE_PROCESSED");
        ResponseEntity<Void> actual = sut.documentWebhookEvent(documentWebhookEvent);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    @DisplayName("400: error when data object is not present in request body")
    public void testErrorEventNullData() {
        DocumentWebhookEvent documentWebhookEvent = new DocumentWebhookEvent();
        documentWebhookEvent.setData(null);
        documentWebhookEvent.setEvent("FILE_PROCESSED");
        Assertions.assertThrows(AiReviewerDocumentException.class, () -> sut.documentWebhookEvent(documentWebhookEvent));
    }

    @Test
    @DisplayName("400: error when data object is empty in request body")
    public void testErrorEventEmptyData() {
        DocumentWebhookEvent documentWebhookEvent = new DocumentWebhookEvent();
        documentWebhookEvent.setData(new ArrayList<>());
        documentWebhookEvent.setEvent("FILE_PROCESSED");
        Assertions.assertThrows(AiReviewerDocumentException.class, () -> sut.documentWebhookEvent(documentWebhookEvent));
    }
}
