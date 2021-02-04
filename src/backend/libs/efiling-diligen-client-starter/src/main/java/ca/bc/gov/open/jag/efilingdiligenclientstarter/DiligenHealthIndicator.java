package ca.bc.gov.open.jag.efilingdiligenclientstarter;

import ca.bc.gov.open.jag.efilingdiligenclient.api.HealthCheckApi;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class DiligenHealthIndicator implements HealthIndicator {

    private HealthCheckApi healthCheckApi;

    @Override
    public Health health() {

        return healthCheckApi.apiIsServerUpGet();


    }
}
