package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtractNotification;
import ca.bc.gov.open.jag.aireviewercsoapi.service.ProcessedDocumentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtractApiDelegateImplTest {

    ExtractApiDelegateImpl sut;

    @Mock
    ProcessedDocumentService processedDocumentService;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new ExtractApiDelegateImpl(processedDocumentService);

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withRequestReturnResult() {

        ResponseEntity actual = sut.extractNotification(UUID.randomUUID(), new ExtractNotification());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }
}
