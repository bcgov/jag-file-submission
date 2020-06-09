package ca.bc.gov.open.jagefilingapi.cache;

import ca.bc.gov.open.api.model.GenerateUrlRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisStorageService implements StorageService<GenerateUrlRequest> {

    private final CacheManager cacheManager;
    private static final String serviceUnavailableMessage = "redis service unavailable";

    /**
     * Default constructor
     *
     * @param cacheManager a spring cache manager
     */
    public RedisStorageService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public String put(GenerateUrlRequest content) {

        UUID id = UUID.randomUUID();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            this.cacheManager.getCache(Keys.FLA_CACHE_NAME).put(id.toString(), objectMapper.writeValueAsString(content));
        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        } catch (JsonProcessingException e) {
            throw new FlaRedisException("error while deserializing object", e.getCause());
        }

        return id.toString();

    }

    @Override
    public GenerateUrlRequest getByKey(String key) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            Cache.ValueWrapper valueWrapper = this.cacheManager.getCache(Keys.FLA_CACHE_NAME).get(key);

            if(valueWrapper == null)
                return null;

            return objectMapper.readValue(valueWrapper.get().toString(), GenerateUrlRequest.class);

        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        } catch (JsonProcessingException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        }

    }

    @Override
    public void deleteByKey(String key) {

        if (key == null || key.isEmpty()) return;
        try {
            this.cacheManager.getCache(Keys.FLA_CACHE_NAME).evict(key);
        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        }
    }

}
