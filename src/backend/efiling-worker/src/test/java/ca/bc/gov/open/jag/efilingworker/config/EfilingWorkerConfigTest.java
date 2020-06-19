package ca.bc.gov.open.jag.efilingworker.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EfilingWorkerConfig Test Suite")
public class EfilingWorkerConfigTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(EfilingWorkerConfig.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(EfilingWorkerConfig.class);
        });
    }
}
