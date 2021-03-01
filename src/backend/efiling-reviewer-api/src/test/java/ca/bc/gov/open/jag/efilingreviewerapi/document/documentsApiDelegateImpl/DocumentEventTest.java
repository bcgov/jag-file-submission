package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentEventTest {

    private DocumentsApiDelegateImpl sut;
    @Mock
    private DiligenService diligenServiceMock;
    @Mock
    private ExtractStore extractStoreMock;
    @Mock
    private ClamAvService clamAvServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        ExtractMapper extractMapper = new ExtractMapperImpl();
        ExtractRequestMapper extratRequestMapper = new ExtractRequestMapperImpl(extractMapper);
        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extratRequestMapper, extractStoreMock, clamAvServiceMock);

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


}
