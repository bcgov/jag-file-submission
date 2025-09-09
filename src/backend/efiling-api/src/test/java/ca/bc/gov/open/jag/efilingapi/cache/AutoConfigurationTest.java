package ca.bc.gov.open.jag.efilingapi.cache;

import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
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
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CacheConfiguration Test Suite")
public class AutoConfigurationTest {

    private static final String TEST_CRED = "notapassword";
    private static final String HOST = "notip";

    @Mock
    private RedisProperties redisProperties;

    private CacheConfiguration sut;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private CacheProperties.Redis redisPropertiesMock;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(redisPropertiesMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redisPropertiesMock);

        sut = new CacheConfiguration(cachePropertiesMock);
        redisProperties = Mockito.mock(RedisProperties.class);

    }

    @DisplayName("CASE1: stand alone input should generate jedisConnectionFactory")
    @Test
    public void standaloneInputShouldGenerateJedisConnectionFactory() {
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getSentinel()).thenReturn(null);
        Mockito.when(redisProperties.getHost()).thenReturn(HOST);
        Mockito.when(redisProperties.getPort()).thenReturn(6379);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("CASE2: cluster input should generate jedisConnectionFactory")
    @Test
    public void clusterInputshouldGenerateJedisConnectionFactory() {
        RedisProperties.Cluster cluster = Mockito.mock(RedisProperties.Cluster.class);
        Mockito.when(redisProperties.getCluster()).thenReturn(cluster);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("CASE3: sentinel input should generate jedisConnectionFactory")
    @Test
    public void sentinelInputShouldGenerateJedisConnectionFactory() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("CASE3: sentinel input should generate exception")
    @Test
    public void sentinelInputShouldThrowIllegalState() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(sentinel.getNodes()).thenReturn(Arrays.asList("TEST:TEST","TEST:TEST"));
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            sut.jedisConnectionFactory(redisProperties);
        });
    }

    @DisplayName("CASE4: correct input should return submission cacheManager")
    @Test
    public void correctInputShouldReturnCacheManager() {
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        CacheManager cacheManager = sut.submissionCacheManager(jedisConnectionFactory, new Jackson2JsonRedisSerializer(Submission.class));
        Assertions.assertNotNull(cacheManager);
    }

    @DisplayName("CASE5: Return correct class")
    @Test
    public void correctReturnJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer actual = sut.jackson2JsonRedisSerializer();
        Assertions.assertNotNull(actual);
    }

    @DisplayName("OK: correct input should return document cacheManager")
    @Test
    public void correctInputShouldReturnDocumentCacheManager() {
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        CacheManager cacheManager = sut.documentCacheManager(jedisConnectionFactory);
        Assertions.assertNotNull(cacheManager);
    }

    @DisplayName("OK: correct input should return documentTypeDetails cacheManager")
    @Test
    public void correctInputShouldReturnDocumentTypeDetailsCacheManager() {
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        CacheManager cacheManager = sut.documentTypeDetailsCacheManager(jedisConnectionFactory, new Jackson2JsonRedisSerializer(DocumentTypeDetails.class));
        Assertions.assertNotNull(cacheManager);
    }


    @DisplayName("OK: should return DocumentTypeDetails Serializer")
    @Test
    public void correctReturnDocumentDetailsJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer actual = sut.documentTypeDetailsSerializer();
        Assertions.assertNotNull(actual);
    }
}
