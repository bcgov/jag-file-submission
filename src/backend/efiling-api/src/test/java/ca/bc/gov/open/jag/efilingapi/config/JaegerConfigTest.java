package ca.bc.gov.open.jagefilingapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JaegerConfig Test Suite")
public class JaegerConfigTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(JaegerConfig.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(JaegerConfig.class);
        });
    }
}
