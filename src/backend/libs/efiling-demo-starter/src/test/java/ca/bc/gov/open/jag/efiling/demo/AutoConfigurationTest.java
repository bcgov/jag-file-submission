package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.bceid.starter.account.BCeIDAccountService;
import ca.bc.gov.open.bceid.starter.account.GetAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {

    private ApplicationContextRunner context;

    @BeforeEach
    public void beforeAll() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(AutoConfiguration.class)
                .withBean(JedisConnectionFactory.class);
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
            assertThat(it).hasSingleBean(EfilingReviewService.class);
            assertThat(it).hasBean("demoAccountCacheManager");
            assertThat(it).hasBean("demoDocumentCacheManager");
            assertThat(it).hasSingleBean(Jackson2JsonRedisSerializer.class);
            assertThat(it).hasSingleBean(BCeIDAccountService.class);

            assertThat(it).hasSingleBean(PaymentAdapter.class);

            PaymentAdapter paymentAdapter = it.getBean(PaymentAdapter.class);
            BCeIDAccountService bCeIDAccountService = it.getBean(BCeIDAccountService.class);

            Assertions.assertEquals("APP", paymentAdapter.makePayment(new EfilingPayment(BigDecimal.TEN, BigDecimal.ONE, "invoice", "client")).getApprovalCd());
            Assertions.assertEquals("DEC", paymentAdapter.makePayment(new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, "invoice", "client")).getApprovalCd());

            GetAccountRequest request = GetAccountRequest.BusinessSelfRequest("test");
            Assertions.assertEquals("efilehub test account", bCeIDAccountService.getIndividualIdentity(request).get().getName().getFirstName());

            assertThat(it).hasSingleBean(SftpService.class);

            SftpService sut = it.getBean(SftpService.class);

            Assertions.assertDoesNotThrow(() -> sut.put(new ByteArrayInputStream("test".getBytes()), "location"));
            Assertions.assertDoesNotThrow(() -> sut.getContent("any"));
            Assertions.assertDoesNotThrow(() -> sut.listFiles("any"));
            Assertions.assertDoesNotThrow(() -> sut.moveFile("a", "b"));


        });
    }

}
