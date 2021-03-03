package ca.bc.gov.open.jag.efilingreviewerapi.cache;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import ca.bc.gov.open.jag.efilingreviewerapi.queue.Receiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.ArrayList;
import java.util.List;

@Configuration

@EnableCaching
public class CacheConfig {

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
    @Bean(name = "extractRequestCacheManager")
    @Primary
    public CacheManager extractRequestCacheManager(
            JedisConnectionFactory jedisConnectionFactory,
            @Qualifier("extractRequestSerializer") Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "extractRequestSerializer")
    @Primary
    public Jackson2JsonRedisSerializer extractRequestSerializer() {
        return new Jackson2JsonRedisSerializer(ExtractRequest.class);
    }

}
