package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.AutoConfiguration;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(AutoConfiguration.class);

    @Test
    public void testConfigure() throws Exception {

        context.run(it -> {
            assertThat(it).hasSingleBean(PaymentsApi.class);
            assertThat(it).hasSingleBean(ApiClient.class);
        });

    }

}
