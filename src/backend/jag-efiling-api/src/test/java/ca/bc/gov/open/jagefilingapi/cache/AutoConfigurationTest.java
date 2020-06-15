package ca.bc.gov.open.jagefilingapi.cache;

import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CacheConfiguration Test Suite")
public class AutoConfigurationTest {
    private static final String PASSWORD = "password";
    @Mock
    NavigationProperties navigationProperties;

    @Mock
    private RedisProperties redisProperties;

    CacheConfiguration autoConfiguration;

    @Mock
    CacheProperties cachePropertiesMock;

    @Mock
    CacheProperties.Redis redisPropertiesMock;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(redisPropertiesMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redisPropertiesMock);

        autoConfiguration = new CacheConfiguration(cachePropertiesMock);
        redisProperties = Mockito.mock(RedisProperties.class);

    }
    @DisplayName("CASE1: stand alone input should generate jedisConnectionFactory")
    @Test
    public void standaloneInputShouldGenerateJedisConnectionFactory() {
        Mockito.when(redisProperties.getHost()).thenReturn("127.0.0.1");
        Mockito.when(redisProperties.getPort()).thenReturn(6379);
        Mockito.when(redisProperties.getPassword()).thenReturn(PASSWORD);
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }
    @DisplayName("CASE2: cluster input should generate jedisConnectionFactory")
    @Test
    public void clusterInputshouldGenerateJedisConnectionFactory() {
        RedisProperties.Cluster cluster = Mockito.mock(RedisProperties.Cluster.class);
        Mockito.when(redisProperties.getCluster()).thenReturn(cluster);
        Mockito.when(redisProperties.getPassword()).thenReturn(PASSWORD);
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }
    @DisplayName("CASE3: sentinel input should generate jedisConnectionFactory")
    @Test
    public void sentinelInputShouldGenerateJedisConnectionFactory() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getPassword()).thenReturn(PASSWORD);
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("CASE4: correct input should return cacheManager")
    @Test
    public void correctInputShouldReturnCacheManager() {
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        CacheManager cacheManager = autoConfiguration.submissionCacheManager(jedisConnectionFactory, new Jackson2JsonRedisSerializer(Submission.class));
        Assertions.assertNotNull(cacheManager);
    }

}
