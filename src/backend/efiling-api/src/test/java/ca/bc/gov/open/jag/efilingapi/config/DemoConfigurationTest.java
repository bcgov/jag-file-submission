package ca.bc.gov.open.jag.efilingapi.config;

import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class DemoConfigurationTest {


    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(ApplicationConfiguration.class);

    @Test
    @DisplayName("Test that the demo implementation of the account service is registered")
    public void testBeansAreGenerated() {
        context.withPropertyValues("jag.efiling.global.demo=true").run(it -> {
            assertThat(it).hasSingleBean(EfilingAccountService.class);
        });
    }

}
