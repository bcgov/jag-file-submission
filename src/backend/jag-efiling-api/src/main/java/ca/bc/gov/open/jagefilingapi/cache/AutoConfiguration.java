package ca.bc.gov.open.jagefilingapi.cache;

import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
@EnableConfigurationProperties(NavigationProperties.class)
public class AutoConfiguration {
    private final NavigationProperties navigationProperties;

    public AutoConfiguration(NavigationProperties navigationProperties) {
        this.navigationProperties = navigationProperties;
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
    @Bean
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(navigationProperties.getExpiryTime()));
        redisCacheConfiguration.usePrefix();

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
    }

}
