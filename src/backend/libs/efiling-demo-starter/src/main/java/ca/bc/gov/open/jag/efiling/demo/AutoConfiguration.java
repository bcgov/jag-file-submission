package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ca.bc.gov.open.jag.efilingcommons.soap.service.*;

@Configuration
@ComponentScan
public class AutoConfiguration {

    @Bean
    public EfilingAccountService efilingAccountService() {
        return new EfilingAccountServiceDemoImpl();
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

}
