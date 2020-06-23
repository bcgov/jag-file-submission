package ca.bc.gov.open.jag.efilingapi.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalCorsConfiguration Test Suite")
public class GlobalCorsConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(GlobalCorsConfiguration.class);

    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        context.run(it -> {
            assertThat(it).hasSingleBean(GlobalCorsConfiguration.class);
        });
    }
}
