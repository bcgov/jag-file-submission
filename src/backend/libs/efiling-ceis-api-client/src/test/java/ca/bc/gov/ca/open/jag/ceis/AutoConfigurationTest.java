package ca.bc.gov.ca.open.jag.ceis;

import ca.bc.gov.open.jag.ceis.AutoConfiguration;
import ca.bc.gov.open.jag.ceis.CeisCourtLocationMapper;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
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
    public void testConfigure() throws Exception {

        context.run(it -> {
            assertThat(it).hasSingleBean(DefaultApi.class);
            assertThat(it).hasSingleBean(ApiClient.class);
            assertThat(it).hasSingleBean(CeisCourtLocationMapper.class);
            assertThat(it).hasSingleBean(EfilingCourtLocationService.class);
        });

    }

}
