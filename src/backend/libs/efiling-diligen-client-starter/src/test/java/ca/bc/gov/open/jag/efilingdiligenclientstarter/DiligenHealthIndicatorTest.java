package ca.bc.gov.open.jag.efilingdiligenclientstarter;

import ca.bc.gov.open.jag.efilingdiligenclient.api.HealthCheckApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse200;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiligenHealthIndicatorTest {

    private DiligenHealthIndicator sut;

    @Mock
    private HealthCheckApi healthCheckApiMock;


    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DiligenHealthIndicator(healthCheckApiMock);

    }

    @Test
    @DisplayName("ok: with diligen up should return up")
    public void withDiligenUpShouldReturnUp() throws ApiException {

        InlineResponse200 inlineResponse200 = new InlineResponse200();
        inlineResponse200.setAnswer("yes");

        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenReturn(inlineResponse200);

        Health result = sut.health();

        Assertions.assertEquals("UP", result.getStatus().getCode());
        Assertions.assertEquals("UP", result.getDetails().get("Diligen Api"));

    }

    @Test
    @DisplayName("error: when diligen return 200 but answer is not yes")
    public void withAnserNoYesShouldReturnDown() throws ApiException {

        InlineResponse200 inlineResponse200 = new InlineResponse200();
        inlineResponse200.setAnswer("it’s not wise to upset a Wookiee.");

        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenReturn(inlineResponse200);

        Health result = sut.health();

        Assertions.assertEquals("DOWN", result.getStatus().getCode());
        Assertions.assertEquals("DOWN", result.getDetails().get("Diligen Api"));
        Assertions.assertEquals("200 OK", result.getDetails().get("StatusCode"));
        Assertions.assertEquals("unknown", result.getDetails().get("Error"));


    }

    @Test
    @DisplayName("error: when diligen return 4xx, 5xx")
    public void withDiligenReturningErrorHttpStatus() throws ApiException {

        InlineResponse200 inlineResponse200 = new InlineResponse200();
        inlineResponse200.setAnswer("it’s not wise to upset a Wookiee.");

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        Mockito.when(healthCheckApiMock.apiIsServerUpGet()).thenThrow(new ApiException(400, headers, "Let’s keep a little optimism here."));

        Health result = sut.health();

        Assertions.assertEquals("DOWN", result.getStatus().getCode());
        Assertions.assertEquals("DOWN", result.getDetails().get("Diligen Api"));
        Assertions.assertEquals("400", result.getDetails().get("StatusCode"));
        Assertions.assertEquals("Let’s keep a little optimism here.", result.getDetails().get("Error"));


    }



}
