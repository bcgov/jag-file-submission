package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.DocumentApi;
import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jagefilingapi.cache.RedisStorageService;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.fee.FeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@RestController
@EnableConfigurationProperties(NavigationProperties.class)
public class DocumentApiImpl implements DocumentApi {

    Logger logger = LoggerFactory.getLogger(DocumentApiImpl.class);

    private final RedisStorageService redisStorageService;
  
    private final NavigationProperties navigationProperties;

    private final FeeService feeService;

    public DocumentApiImpl(RedisStorageService redisStorageService, NavigationProperties navigationProperties,
                           FeeService feeService) {
        this.redisStorageService = redisStorageService;
        this.navigationProperties = navigationProperties;
        this.feeService = feeService;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {
        logger.info("Generate Url Request Recieved");
        //TODO: We should a service

        logger.debug("Attempting to get fee structure for document");
        Fee fee = feeService.getFee(new FeeRequest("type", "subtype"));
        logger.info("Successfully retrived fee [{}]", fee.getAmount().toString());

        GenerateUrlResponse response = new GenerateUrlResponse();

        response.expiryDate(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(navigationProperties.getExpiryTime()));
        response.setEFilingUrl(MessageFormat.format("{0}/{1}", navigationProperties.getBaseUrl(), redisStorageService.put(generateUrlRequest)));



        logger.debug("{}", response);

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<GenerateUrlRequest> getConfigurationById(String id) {
        GenerateUrlRequest generateUrlRequest =  redisStorageService.getByKey(id);
        if (generateUrlRequest != null) {
            return ResponseEntity.ok(generateUrlRequest);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
