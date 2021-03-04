package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Receiver test suite")
public class ReceiverTest {

    Receiver sut;

    @Mock
    private DocumentsApiDelegate documentsApiDelegateMock;

    @Mock
    private DiligenService diligenServiceMock;

    @Mock
    private StringRedisTemplate stringRedisTemplateMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(documentsApiDelegateMock.documentEvent(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        Mockito.doNothing().when(stringRedisTemplateMock).convertAndSend(any(), any());

        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(diligenDocumentDetails("PROCESSED"));

        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.TEN))).thenReturn(diligenDocumentDetails("QUEUED_FOR_ML_ANALYSIS"));

        Mockito.when(diligenServiceMock.getDocumentDetails(ArgumentMatchers.eq(BigDecimal.ZERO))).thenReturn(diligenDocumentDetails("ERROR"));

        sut = new Receiver(1, documentsApiDelegateMock, diligenServiceMock, stringRedisTemplateMock);

    }

    @Test
    @DisplayName("Ok: Message processed")
    public void withMessageItIsProcessed() {

        Assertions.assertDoesNotThrow(() -> sut.receiveMessage("1"));

    }

    @Test
    @DisplayName("Ok: Message re-added")
    public void withMessageAddedToRedis() {

        Assertions.assertDoesNotThrow(() -> sut.receiveMessage("10"));

    }

    @Test
    @DisplayName("Ok: Exit On Status")
    public void withExitOnStatus() {

        Assertions.assertDoesNotThrow(() -> sut.receiveMessage("0"));

    }

    private DiligenDocumentDetails diligenDocumentDetails(String status) {
        return DiligenDocumentDetails.builder()
                .fileStatus(status)
                .create();
    }

}
