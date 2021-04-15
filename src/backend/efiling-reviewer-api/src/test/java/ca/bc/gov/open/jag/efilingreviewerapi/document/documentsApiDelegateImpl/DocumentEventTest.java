package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponseData;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentConfigException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
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
public class DocumentEventTest {


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

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        ExtractMapper extractMapper = new ExtractMapperImpl();
        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl(extractMapper);

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode result = objectMapper.createObjectNode();
        result.put("test", "test");

        Mockito.when(fieldProcessorMock.getJson(Mockito.any(), Mockito.any())).thenReturn(result);

        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(DiligenDocumentDetails.builder().create());
        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.TEN))).thenReturn(DiligenDocumentDetails.builder().create());


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
                .create()));

        Mockito.when(extractStoreMock.get(Mockito.eq(BigDecimal.TEN))).thenReturn(Optional.of(ExtractRequest.builder()
                .document(Document.builder()
                        .type("NO-CONFIG")
                        .create())
                .create()));

        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extractRequestMapper, extractStoreMock, stringRedisTemplateMock, fieldProcessorMock, documentValidatorMock, documentTypeConfigurationRepositoryMock, null);

    }

    @Test()
    @DisplayName("204: accept any event")
    public void testAnyEvent() {

        DocumentEvent documentEvent = new DocumentEvent();
        documentEvent.setDocumentId(BigDecimal.ONE);
        documentEvent.setStatus("Processed");
        ResponseEntity<Void> actual = sut.documentEvent(UUID.randomUUID(), documentEvent);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    @DisplayName("400: error when event is not present")
    public void testErrorEvent() {

        DocumentEvent documentEvent = new DocumentEvent();
        documentEvent.setDocumentId(BigDecimal.TEN);
        documentEvent.setStatus("Processed");
        Assertions.assertThrows(AiReviewerDocumentConfigException.class, () -> sut.documentEvent(UUID.randomUUID(), documentEvent));

    }


}
