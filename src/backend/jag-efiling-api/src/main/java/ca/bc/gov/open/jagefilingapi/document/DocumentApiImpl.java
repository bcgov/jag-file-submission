package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.DocumentApi;
import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jagefilingapi.cache.RedisStorageService;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@EnableConfigurationProperties(NavigationProperties.class)
public class DocumentApiImpl implements DocumentApi {

    Logger logger = LoggerFactory.getLogger(DocumentApiImpl.class);

    private final RedisStorageService redisStorageService;
  
    private final NavigationProperties navigationProperties;

    public DocumentApiImpl(RedisStorageService redisStorageService, NavigationProperties navigationProperties) {
        this.redisStorageService = redisStorageService;
        this.navigationProperties = navigationProperties;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {
        logger.info("Generate Url Request Recieved");
        //TODO: We should a service
        GenerateUrlResponse response = new GenerateUrlResponse();

        response.setStorageId(redisStorageService.put(generateUrlRequest));
        response.expiryDate(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        response.setEFilingUrl(navigationProperties.getBaseUrl());

        logger.debug("{}", response.toString());

        return ResponseEntity.ok(response);

    }
}
