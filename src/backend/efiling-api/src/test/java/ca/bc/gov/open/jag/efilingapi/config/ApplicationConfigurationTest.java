package ca.bc.gov.open.jag.efilingapi.config;

import io.jaegertracing.internal.JaegerTracer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Application Configuration Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationConfigurationTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(ApplicationConfiguration.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(JaegerTracer.class);
        });
    }

}
