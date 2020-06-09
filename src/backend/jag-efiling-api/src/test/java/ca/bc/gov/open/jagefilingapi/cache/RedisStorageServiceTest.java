package ca.bc.gov.open.jagefilingapi.cache;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("RedisStorageService Test Suite")
public class RedisStorageServiceTest {

    private static final byte[] VALID = "valid".getBytes();
    private static final byte[] EXCEPTION_INPUT = "exception".getBytes();
    private static final String KEY = "key";
    private static final String EMPTY_KEY = "";
    private static final String NULL_KEY = null;
    private static final String MISSING_DOCUMENT = "MISSING_DOCUMENT";
    private static final String REDIS_CONNECTION_FAILURE_EXCEPTION = "RedisConnectionFailureException";

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Cache.ValueWrapper valueWrapper;

    private RedisStorageService sut;

    @BeforeAll
    public void Init() {

        MockitoAnnotations.initMocks(this);
        Mockito.when(valueWrapper.get()).thenReturn(VALID);
        Mockito.when(cacheManager.getCache(Keys.FLA_CACHE_NAME)).thenReturn(this.cache);
        Mockito.when(cache.get(KEY)).thenReturn(valueWrapper);
        Mockito.when(cache.get(MISSING_DOCUMENT)).thenReturn(null);
        Mockito.doNothing().when(this.cache).put(Mockito.anyString(), Mockito.eq(VALID));
        Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).put(Mockito.anyString(), Mockito.eq(EXCEPTION_INPUT));
        Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).get(Mockito.eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
        Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).evict(Mockito.eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
        Mockito.doNothing().when(this.cache).evict(KEY);
        this.sut = new RedisStorageService(cacheManager);

    }

    @DisplayName("CASE1: put with valid content should not throw Exception")
    @Test
    public void putWithValidContentShouldNotThrowException() throws Exception {
        Assertions.assertDoesNotThrow(() -> sut.put(VALID) );
    }
    @DisplayName("CASE2: put with redis connection failure exception should throw FlaRedisException")
    @Test
    public void putWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() throws Exception {
        Assertions.assertThrows(FlaRedisException.class, () -> sut.put(EXCEPTION_INPUT));
    }
    @DisplayName("CASE3: get with existing key should get Bytes")
    @Test
    public void getWithExistingKeyShouldGetBytes() {
        byte[] result = sut.get(KEY);
        Assertions.assertEquals(new String(VALID), new String(result));
    }
    @DisplayName("CASE4: get with existing key should return Null")
    @Test
    public void getWithNonExistingKeyShouldReturnNull() {
        Assertions.assertNull(sut.get(MISSING_DOCUMENT));
    }

    @DisplayName("CASE5: get with Redis connection failure exception should throw FlaRedisException")
    @Test
    public void getWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() throws Exception {
        Assertions.assertThrows(FlaRedisException.class, () -> sut.get(REDIS_CONNECTION_FAILURE_EXCEPTION));
    }
    @DisplayName("CASE6: delete with Redis connection failure exception should throw FlaRedisException")
    @Test
    public void deleteWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() throws Exception {
        Assertions.assertThrows(FlaRedisException.class, () -> sut.delete(REDIS_CONNECTION_FAILURE_EXCEPTION));
    }
    @DisplayName("CASE7: delete with any key should not throw")
    @Test
    public void deleteWithAnyKeyShouldNotThrow() throws Exception {
        Assertions.assertDoesNotThrow(() -> sut.delete(KEY));
        Assertions.assertDoesNotThrow(() -> sut.delete(EMPTY_KEY));
        Assertions.assertDoesNotThrow(() -> sut.delete(NULL_KEY));
    }
    @DisplayName("CASE8: delete with empty or null should not call cache")
    @Test
    public void deleteWithEmptyOrNullShouldNotCallCache() throws Exception {
        sut.delete(EMPTY_KEY);
        sut.delete(NULL_KEY);
        Mockito.verify(cache, Mockito.times(0))
                .evict(Mockito.eq(EMPTY_KEY));
        Mockito.verify(cache, Mockito.times(0))
                .evict(Mockito.eq(NULL_KEY));
    }
    @DisplayName("CASE9: delete with valid string should call cache")
    @Test
    public void deleteWithValidStringShouldCallCache() throws Exception {
        sut.delete(KEY);
        Mockito.verify(cache, Mockito.times(1))
                .evict(Mockito.eq(KEY));
    }

}
