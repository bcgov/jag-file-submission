package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.AutoConfiguration;

import org.openapitools.client.api.PaymentsApi;
import org.openapitools.client.ApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AutoConfiguration")
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
