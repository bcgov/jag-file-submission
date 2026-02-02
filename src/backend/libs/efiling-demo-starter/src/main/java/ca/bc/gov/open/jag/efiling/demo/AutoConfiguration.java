package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.bceid.starter.account.BCeIDAccountService;
import ca.bc.gov.open.bceid.starter.account.GetAccountRequest;
import ca.bc.gov.open.bceid.starter.account.models.IndividualIdentity;
import ca.bc.gov.open.bceid.starter.account.models.Name;
import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@ComponentScan
public class AutoConfiguration {


    public static final String TRANSACTION_STATE_DECLINED = "DEC";
    public static final String TRANSACTION_STATE_APPROVED = "APP";

    @Bean
    public EfilingAccountService efilingAccountService(AccountDetailsCache accountDetailsCache) {
        return new EfilingAccountServiceDemoImpl(accountDetailsCache);
    }

    @Bean
    public EfilingLookupService efilingLookupService() {
        return new EfilingLookupServiceDemoImpl();
    }

    @Bean
    public EfilingDocumentService efilingDocumentService() { return new EfilingDocumentServiceDemoImpl(); }

    @Bean
    public EfilingCourtService efilingCourtService() { return new EfilingCourtServiceDemoImpl(); }

    @Bean
    public EfilingSubmissionService efilingSubmissionService() { return new EfilingSubmissionServiceDemoImpl(); }

    @Bean
    public EfilingCourtLocationService efilingCourtLocationService() { return new EfilingCourtLocationServiceDemoImpl(); }

    @Bean
    public EfilingReviewService efilingReviewService() { return new EfilingReviewServiceDemoImpl(); }

    @Bean
    public AccountDetailsCache accountDetailsCache() { return new AccountDetailsCacheImpl(); }

    @Bean
    public EfilingSearchService efilingSearchService() { return new EfilingSearchServiceDemoImpl(); }

    /**
     * Configures the cache manager for demo accounts
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "demoAccountCacheManager")
    public CacheManager demoAccountCacheManager(JedisConnectionFactory jedisConnectionFactory,
                                                @Qualifier("accountSerializer") Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();

    }

    @Bean(name = "accountSerializer")
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer(AccountDetails.class);
    }

    /**
     * Configures the cache manager
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "demoDocumentCacheManager")
    public CacheManager demoDocumentCacheManager(
            JedisConnectionFactory jedisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(24));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "demoSftpService")
    @Primary
    public SftpService sftpService() {
        return new SftpService() {
            @Override
            public ByteArrayInputStream getContent(String s) {
                return null;
            }

            @Override
            public void moveFile(String s, String s1) {
                // not in use in demo so left without implementation
            }

            @Override
            @Cacheable(cacheNames = "demoDocument", key = "#s", cacheManager = "demoDocumentCacheManager", unless = "#result == null")
            public void put(InputStream inputStream, String s) {
                // not in use in demo so left without implementation
            }

            @Override
            public List<String> listFiles(String s) {
                return new ArrayList<>();
            }
        };
    }

    @Bean
    public BCeIDAccountService bCeIDAccountService() {
        return new BCeIDAccountService() {
            @Override
            public Optional<IndividualIdentity> getIndividualIdentity(GetAccountRequest getAccountRequest) {
                IndividualIdentity individualIdentity = IndividualIdentity.builder().name(Name.builder().firstName("efilehub test account").middleName("").surname("efile tester").create()).create();
                return Optional.of(individualIdentity);
            }
        };
    }


    @Bean
    public PaymentAdapter paymentAdapter() {
        return new PaymentAdapter() {
            @Override
            public PaymentTransaction makePayment(EfilingPayment efilingPayment) {
                PaymentTransaction paymentTransaction = new PaymentTransaction();
                if(efilingPayment.getPaymentAmount().equals(new BigDecimal(10))) {
                    paymentTransaction.setApprovalCd(TRANSACTION_STATE_DECLINED);
                } else {
                    paymentTransaction.setApprovalCd(TRANSACTION_STATE_APPROVED);
                }
                return paymentTransaction;

            }

            @Override
            public PaymentProfile createProfile(EfilingPaymentProfile efilingPaymentProfile) {
                return new PaymentProfile(BigDecimal.ONE, 0, "Approved", "123");
            }

            @Override
            public PaymentProfile updateProfile(EfilingPaymentProfile efilingPaymentProfile) {
                return new PaymentProfile(BigDecimal.ONE, 0, "Approved", "123");
            }
        };
    }


}
