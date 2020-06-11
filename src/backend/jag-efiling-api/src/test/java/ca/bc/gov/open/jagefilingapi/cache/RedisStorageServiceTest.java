package ca.bc.gov.open.jagefilingapi.cache;

import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.Navigation;
import ca.bc.gov.open.api.model.Redirect;
import ca.bc.gov.open.jagefilingapi.Keys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("RedisStorageService Test Suite")
public class RedisStorageServiceTest {

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

    private RedisStorageService<GenerateUrlRequest> sut;

    @BeforeAll
    public void init() {

        MockitoAnnotations.initMocks(this);
        when(valueWrapper.get()).thenReturn(toJson(getGenerateUrlRequest()));
        when(cacheManager.getCache(Keys.SECURE_URL_CACHE_NAME)).thenReturn(this.cache);
        when(cache.get(KEY)).thenReturn(valueWrapper);
        when(cache.get(MISSING_DOCUMENT)).thenReturn(null);
        doThrow(RedisConnectionFailureException.class).when(this.cache).put(anyString(), eq(EXCEPTION_INPUT));
        doThrow(RedisConnectionFailureException.class).when(this.cache).get(eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
        doThrow(RedisConnectionFailureException.class).when(this.cache).evict(eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
        doNothing().when(this.cache).evict(KEY);
        this.sut = new RedisStorageService<>(cacheManager);

    }

    @DisplayName("CASE1: put with valid content should not throw Exception")
    @Test
    public void putWithValidContentShouldNotThrowException() {

        doNothing().when(this.cache).put(anyString(), anyString());

        GenerateUrlRequest request = getGenerateUrlRequest();

        Assertions.assertDoesNotThrow(() -> sut.put(request) );
    }

    @DisplayName("CASE2: put with redis connection failure exception should throw EFilingRedisException")
    @Test
    public void putWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() {
        doThrow(RedisConnectionFailureException.class).when(this.cache).put(anyString(), anyString());
        Assertions.assertThrows(EFilingRedisException.class, () -> sut.put(getGenerateUrlRequest()));
    }

    @DisplayName("CASE3: get with existing key should get Bytes")
    @Test
    public void getWithExistingKeyShouldGetBytes() {
        GenerateUrlRequest actual = sut.getByKey(KEY, GenerateUrlRequest.class);
        Assertions.assertEquals("http://error", actual.getNavigation().getError().getUrl());
    }
    @DisplayName("CASE4: get with existing key should return Null")
    @Test
    public void getWithNonExistingKeyShouldReturnNull() {
        Assertions.assertNull(sut.getByKey(MISSING_DOCUMENT, GenerateUrlRequest.class));
    }

    @DisplayName("CASE5: get with Redis connection failure exception should throw EFilingRedisException")
    @Test
    public void getWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() {
        Assertions.assertThrows(EFilingRedisException.class, () -> sut.getByKey(REDIS_CONNECTION_FAILURE_EXCEPTION, GenerateUrlRequest.class));
    }

    @DisplayName("CASE6: delete with Redis connection failure exception should throw EFilingRedisException")
    @Test
    public void deleteWithRedisConnectionFailureExceptionShouldThrowFlaRedisException() {
        Assertions.assertThrows(EFilingRedisException.class, () -> sut.getByKey(REDIS_CONNECTION_FAILURE_EXCEPTION, GenerateUrlRequest.class));
    }

    @DisplayName("CASE7: delete with any key should not throw")
    @Test
    public void deleteWithAnyKeyShouldNotThrow() {
        Assertions.assertDoesNotThrow(() -> sut.deleteByKey(KEY));
        Assertions.assertDoesNotThrow(() -> sut.deleteByKey(EMPTY_KEY));
        Assertions.assertDoesNotThrow(() -> sut.deleteByKey(NULL_KEY));
    }
    
    @DisplayName("CASE8: delete with empty or null should not call cache")
    @Test
    public void deleteWithEmptyOrNullShouldNotCallCache() {
        sut.deleteByKey(EMPTY_KEY);
        sut.deleteByKey(NULL_KEY);
        Mockito.verify(cache, Mockito.times(0))
                .evict(eq(EMPTY_KEY));
        Mockito.verify(cache, Mockito.times(0))
                .evict(eq(NULL_KEY));
    }
    @DisplayName("CASE9: delete with valid string should call cache")
    @Test
    public void deleteWithValidStringShouldCallCache()  {
        sut.deleteByKey(KEY);
        Mockito.verify(cache, Mockito.times(1))
                .evict(eq(KEY));
    }


    public String toJson(GenerateUrlRequest generateUrlRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(generateUrlRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error!!", e);
        }

    }

    private GenerateUrlRequest getGenerateUrlRequest() {
        GenerateUrlRequest request = new GenerateUrlRequest();
        Navigation navigation = new Navigation();
        Redirect error = new Redirect();
        error.setUrl("http://error");
        navigation.setError(error);
        Redirect cancel = new Redirect();
        cancel.setUrl("http://cancel");
        navigation.setCancel(cancel);
        Redirect success = new Redirect();
        success.setUrl("http://success");
        navigation.setSuccess(success);
        request.setNavigation(navigation);
        return request;
    }

}
