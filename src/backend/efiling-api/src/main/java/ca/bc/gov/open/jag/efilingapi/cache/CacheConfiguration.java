package ca.bc.gov.open.jag.efilingapi.cache;


import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
public class CacheConfiguration {

    private final CacheProperties cacheProperties;

    public CacheConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }
    /**
     * Configure the JedisConnectionFactory
     * @param properties The redis properties
     * @return a JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties properties) {

        if(properties.getCluster() != null) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(properties.getCluster().getNodes());
            redisClusterConfiguration.setPassword(properties.getPassword());

            if(properties.getCluster().getMaxRedirects() != null)
                redisClusterConfiguration.setMaxRedirects(properties.getCluster().getMaxRedirects());

            return new JedisConnectionFactory(redisClusterConfiguration);
        }

        if(properties.getSentinel() != null) {
            RedisSentinelConfiguration redisSantinelConfiguration = new RedisSentinelConfiguration();
            redisSantinelConfiguration.setMaster(properties.getSentinel().getMaster());
            redisSantinelConfiguration.setSentinels(createSentinels(properties.getSentinel()));
            redisSantinelConfiguration.setPassword(properties.getPassword());
            return new JedisConnectionFactory(redisSantinelConfiguration);
        }

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(properties.getHost());
        redisStandaloneConfiguration.setPort(properties.getPort());
        redisStandaloneConfiguration.setPassword(properties.getPassword());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {

        List<RedisNode> nodes = new ArrayList<>();
        for (String node : sentinel.getNodes()) {
            try {
                String[] parts = node.split(":");
                nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            }
            catch (RuntimeException ex) {
                throw new IllegalStateException(
                        "Invalid redis sentinel " + "property '" + node + "'", ex);
            }
        }
        return nodes;
    }

    /**
     * Configures the cache manager
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "submissionCacheManager")
    @Primary
    public CacheManager submissionCacheManager(
            JedisConnectionFactory jedisConnectionFactory,
            @Qualifier("submissionSerializer") Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "submissionSerializer")
    @Primary
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer(Submission.class);
    }

    /**
     * Configures the cache manager
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "documentCacheManager")
    public CacheManager documentCacheManager(
            JedisConnectionFactory jedisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(24));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    /**
     * Configures the cache manager
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "documentTypeDetailsCacheManager")
    public CacheManager documentTypeDetailsCacheManager(
            JedisConnectionFactory jedisConnectionFactory,
            @Qualifier("documentTypeDetailsSerializer") Jackson2JsonRedisSerializer documentTypeDetailsSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(24))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(documentTypeDetailsSerializer));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "documentTypeDetailsSerializer")
    public Jackson2JsonRedisSerializer documentTypeDetailsSerializer() {
        return new Jackson2JsonRedisSerializer(DocumentTypeDetails.class);
    }

    /**
     * Configures the cache manager
     * @param jedisConnectionFactory A jedisConnectionFactory
     * @return
     */
    @Bean(name = "accountDetailsCacheManager")
    public CacheManager accountDetailsCacheManager(
            JedisConnectionFactory jedisConnectionFactory,
            @Qualifier("accountDetailsSerializer") Jackson2JsonRedisSerializer accountDetailsSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(accountDetailsSerializer));;

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "accountDetailsSerializer")
    public Jackson2JsonRedisSerializer accountDetailsSerializer() {
        return new Jackson2JsonRedisSerializer(AccountDetails.class);
    }

}
