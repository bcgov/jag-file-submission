package ca.bc.gov.open.jag.efilingreviewerapi.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.cache.RedisCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "spring.redis.host=localhost",
                    "spring.redis.port=6379",
                    "spring.redis.password=admin",
                    "spring.cache.redis.time-to-live=600000"
            )
            .withUserConfiguration(RedisProperties.class)
            .withUserConfiguration(CacheConfig.class);

    @Test
    @DisplayName("submissionCacheManager test")
    public void testExtractRequestCacheManager() {
        context.run(it -> {
            assertThat(it).hasBean("extractRequestCacheManager");
            assertThat(it).hasBean("extractRequestSerializer");
            RedisCacheManager actualCacheManager = ((RedisCacheManager)it.getBean("extractRequestCacheManager"));
            actualCacheManager.getCache("redis");
        });
    }

}
