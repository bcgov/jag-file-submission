package ca.bc.gov.open.jagefilingapi.submission;

import ca.bc.gov.open.jagefilingapi.cache.RedisStorageService;
import ca.bc.gov.open.jagefilingapi.cache.StorageService;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public StorageService<Submission> submissionStorageService(CacheManager cacheManager) {
        return new RedisStorageService<Submission>(cacheManager);
    }

    @Bean
    public SubmissionService submissionService(StorageService<Submission> submissionStorageService) {
        return new SubmissionServiceImpl(submissionStorageService);
    }

}