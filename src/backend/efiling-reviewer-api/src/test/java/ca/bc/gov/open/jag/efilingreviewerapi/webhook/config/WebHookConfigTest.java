package ca.bc.gov.open.jag.efilingreviewerapi.webhook.config;

import ca.bc.gov.open.jag.efilingreviewerapi.webhook.WebHookService;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.properties.WebHookProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebHookConfigTest {
    ApplicationContextRunner context;

    @BeforeAll
    public void setup() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(WebHookConfig.class)
                .withBean(RestTemplate.class)
                .withPropertyValues("jag.efiling.cso.webhook.basePath=http://test",
                        "jag.efiling.cso.webhook.returnPath=http://test")
                .withUserConfiguration(WebHookProperties.class);
    }

    @Test
    @DisplayName("Test that beans are created")
    public void testBeansAreGenerated() {

        context.run(it -> {
            assertThat(it).hasSingleBean(WebHookService.class);
        });

    }
}
