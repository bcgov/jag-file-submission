package ca.bc.gov.open.jagefilingapi.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisStorageService implements StorageService {

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

    /**
     * Store the content in redis cache using a new guid as key
     */
    @Override
    public String put(byte[] content) {

        UUID id = UUID.randomUUID();

        try {
            this.cacheManager.getCache(Keys.FLA_CACHE_NAME).put(id.toString(), content);
        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        }

        return id.toString();

    }

    /**
     * Gets a document from redis key value store.
     * @param key    object key to retrieve from storage
     */
    @Override
    public byte[] get(String key) {

        try {

            Cache.ValueWrapper valueWrapper = this.cacheManager.getCache(Keys.FLA_CACHE_NAME).get(key);

            if(valueWrapper == null)
                return null;

            return (byte[]) valueWrapper.get();

        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        }

    }

    /**
     * Removes a document from redis key value store.
     * @param key    object key to evict from storage
     */
    @Override
    public void delete(String key) {

        if (key == null || key.isEmpty()) return;
        try {
            this.cacheManager.getCache(Keys.FLA_CACHE_NAME).evict(key);
        } catch (RedisConnectionFailureException e) {
            throw new FlaRedisException(serviceUnavailableMessage, e.getCause());
        }

    }

}
