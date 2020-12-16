package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.bceid.starter.account.BCeIDAccountService;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {

    private ApplicationContextRunner context;

    @BeforeAll
    public void beforeAll() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(AutoConfiguration.class);
    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingAccountServiceDemoImpl")
    public void autoConfigurationShouldReturnDemoAccountServiceImpl() {
        context.run(it -> {
            assertThat(it).hasSingleBean(EfilingAccountService.class);
            assertThat(it).hasSingleBean(EfilingLookupService.class);
            assertThat(it).hasSingleBean(EfilingDocumentService.class);
            assertThat(it).hasSingleBean(EfilingCourtService.class);
            assertThat(it).hasSingleBean(EfilingSubmissionService.class);
            assertThat(it).hasSingleBean(EfilingCourtLocationService.class);
            assertThat(it).hasSingleBean(CacheManager.class);
            assertThat(it).hasSingleBean(Jackson2JsonRedisSerializer.class);
            assertThat(it).hasSingleBean(BCeIDAccountService.class);
            assertThat(it).hasSingleBean(PaymentAdapter.class);
        });
    }


}
