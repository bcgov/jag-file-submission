package ca.bc.gov.open.jag.efilingapi.courts.config;

import ca.bc.gov.open.jag.efilingapi.courts.CourtsConfiguration;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourtsConfiguration test suite")
public class CourtsConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(CourtsConfiguration.class);

    @Test
    public void testConfigure() {

        context.run(it -> {
            assertThat(it).hasSingleBean(CourtLocationMapper.class);
        });

    }
}
