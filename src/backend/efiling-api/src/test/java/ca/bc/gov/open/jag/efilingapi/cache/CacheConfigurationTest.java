package ca.bc.gov.open.jag.efilingapi.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheConfigurationTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "spring.redis.host=localhost",
                    "spring.redis.port=6379",
                    "spring.redis.password=admin",
                    "spring.cache.redis.time-to-live=600000"
            )
            .withUserConfiguration(RedisProperties.class)
            .withUserConfiguration(CacheConfiguration.class);

    @Test
    @DisplayName("submissionCacheManager test")
    public void testSubmissionCacheManager() {
        context.run(it -> {
            assertThat(it).hasBean("submissionCacheManager");
            assertThat(it).hasBean("submissionSerializer");
            RedisCacheManager actualCacheManager = ((RedisCacheManager)it.getBean("submissionCacheManager"));
            actualCacheManager.getCache("redis");
            Assertions.assertEquals(Duration.ofSeconds(600), actualCacheManager.getCacheConfigurations().get("redis").getTtl());

        });
    }

    @Test
    @DisplayName("documentCacheManager test")
    public void testDocumentCacheManager() {
        context.run(it -> {
            assertThat(it).hasBean("documentCacheManager");
            RedisCacheManager actualCacheManager = ((RedisCacheManager)it.getBean("documentCacheManager"));
            actualCacheManager.getCache("redis");
            Assertions.assertEquals(Duration.ofHours(24), actualCacheManager.getCacheConfigurations().get("redis").getTtl());
        });
    }

    @Test
    @DisplayName("documentDetailsCacheManager test")
    public void testDocumentDetailsCacheManager() {
        context.run(it -> {
            assertThat(it).hasBean("documentTypeDetailsCacheManager");
            assertThat(it).hasBean("documentTypeDetailsSerializer");
            RedisCacheManager actualCacheManager = ((RedisCacheManager)it.getBean("documentTypeDetailsCacheManager"));
            actualCacheManager.getCache("redis");
            Assertions.assertEquals(Duration.ofHours(24), actualCacheManager.getCacheConfigurations().get("redis").getTtl());
        });
    }

    @Test
    @DisplayName("accountDetailsCacheManager test")
    public void testAccountDetailsCacheManater() {
        context.run(it -> {
            assertThat(it).hasBean("accountDetailsCacheManager");
            assertThat(it).hasBean("accountDetailsSerializer");
            RedisCacheManager actualCacheManager = ((RedisCacheManager)it.getBean("accountDetailsCacheManager"));
            actualCacheManager.getCache("redis");
            Assertions.assertEquals(Duration.ofMinutes(15), actualCacheManager.getCacheConfigurations().get("redis").getTtl());
        });
    }

}
