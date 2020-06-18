package ca.bc.gov.open.jagefilingapi.fee.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Config Test Suite")
public class ConfigTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(Config.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(Config.class);
        });
    }
}
