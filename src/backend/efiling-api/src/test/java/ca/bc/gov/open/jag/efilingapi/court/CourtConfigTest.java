package ca.bc.gov.open.jag.efilingapi.court;

import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtServiceImpl;
import ca.bc.gov.open.jag.efilingapi.fakes.EfilingCourtServiceFake;
import ca.bc.gov.open.jag.efilingapi.fakes.EfilingSearchServiceFake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(CourtConfig.class)
            .withBean(EfilingCourtServiceFake.class)
            .withBean(EfilingSearchServiceFake.class);

    @Test
    @DisplayName("Test Court Configuration Beans")
    public void testSubmissionBeans() {

        context.run(it -> {

            assertThat(it).hasSingleBean(CourtService.class);
            Assertions.assertEquals(CourtServiceImpl.class, it.getBean(CourtService.class).getClass());

        });

    }

}
