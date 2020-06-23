package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AutoConfiguration Test")
public class AutoConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(AutoConfiguration.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(EfilingAccountService.class);
        });
    }
}
