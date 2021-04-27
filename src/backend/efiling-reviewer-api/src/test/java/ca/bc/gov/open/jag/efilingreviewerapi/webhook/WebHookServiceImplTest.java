package ca.bc.gov.open.jag.efilingreviewerapi.webhook;

import ca.bc.gov.open.jag.efilingreviewerapi.webhook.model.DocumentReady;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.properties.WebHookProperties;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebHookServiceImplTest {

    private static final String FAKE_PATH = "http://test";
    WebHookService sut;

    @Mock
    private RestTemplate restTemplateMock;


    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        WebHookProperties webHookProperties = new WebHookProperties();

        webHookProperties.setBasePath(FAKE_PATH);
        webHookProperties.setReturnPath(FAKE_PATH);


        sut = new WebHookServiceImpl(restTemplateMock, webHookProperties);

    }

    @Test()
    @DisplayName("Success: accept any event")
    public void withParentAppAvailableDocumentReadySent() {

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(), any(Class.class))).thenReturn(ResponseEntity.ok("success"));

        Assertions.assertDoesNotThrow(() -> sut.sendDocumentReady(BigDecimal.ONE, "DOCUMENT TYPE"));

    }

    @Test()
    @DisplayName("Error: message was not sent")
    public void withParentAppDownFiveAttemptsMade() {

        Mockito.when(restTemplateMock.postForEntity(any(String.class), any(), any(Class.class))).thenReturn(ResponseEntity.notFound().build());

        Assertions.assertDoesNotThrow(() -> sut.sendDocumentReady(BigDecimal.ONE, "DOCUMENT TYPE"));

    }

}
