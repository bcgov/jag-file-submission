package ca.bc.gov.open.jag.efilingcourtdocumentapiclient.config;

import ca.bc.gov.open.jag.efilingcourtdocumentapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingcourtdocumentapiclient.api.handler.ApiClient;
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
    public void testConfigure() {

        context.run(it -> {
            assertThat(it).hasSingleBean(DefaultApi.class);
            assertThat(it).hasSingleBean(ApiClient.class);
        });

    }

}
