package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(StoreConfig.class);

    @Test
    @DisplayName("Test Court Configuration Beans")
    public void testSubmissionBeans() {

        context.run(it -> {

            assertThat(it).hasSingleBean(StoreConfig.class);

        });

    }

}