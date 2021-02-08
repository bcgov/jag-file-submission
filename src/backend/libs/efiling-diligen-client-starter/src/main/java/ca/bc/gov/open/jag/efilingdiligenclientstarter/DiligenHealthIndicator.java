package ca.bc.gov.open.jag.efilingdiligenclientstarter;

import ca.bc.gov.open.jag.efilingdiligenclient.api.HealthCheckApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse200;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;

public class DiligenHealthIndicator implements HealthIndicator {

    private static final String DILIGEN_API_KEY = "Diligen Api";
    private static final String STATUS_CODE_KEY = "StatusCode";
    private static final String ERROR_KEY = "Error";

    private static final String DOWN = "DOWN";
    private static final String UP = "UP";

    private final HealthCheckApi healthCheckApi;

    public DiligenHealthIndicator(HealthCheckApi healthCheckApi) {
        this.healthCheckApi = healthCheckApi;
    }

    @Override
    public Health health() {

        try {

            InlineResponse200 inlineResponse200 = healthCheckApi.apiIsServerUpGet();


            if(!StringUtils.equalsIgnoreCase("yes", inlineResponse200.getAnswer())) {
                return Health.down()
                        .withDetail(DILIGEN_API_KEY, DOWN)
                        .withDetail(STATUS_CODE_KEY, HttpStatus.OK.toString())
                        .withDetail(ERROR_KEY, "unknown")
                        .build();

            }

            return Health.up().withDetail(DILIGEN_API_KEY, UP).build();

        } catch (ApiException e) {

            return Health
                    .down()
                    .withDetail(DILIGEN_API_KEY, DOWN)
                    .withDetail(STATUS_CODE_KEY, String.valueOf(e.getCode()))
                    .withDetail(ERROR_KEY, e.getResponseBody())
                    .build();

        }


    }
}
