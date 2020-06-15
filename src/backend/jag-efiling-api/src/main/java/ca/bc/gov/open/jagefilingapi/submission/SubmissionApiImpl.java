package ca.bc.gov.open.jagefilingapi.submission;

import ca.bc.gov.open.jagefilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jagefilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jagefilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.fee.FeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jagefilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionApiImpl implements SubmissionApiDelegate {

    Logger logger = LoggerFactory.getLogger(SubmissionApiImpl.class);

    private final SubmissionService submissionService;

    private final NavigationProperties navigationProperties;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final FeeService feeService;

    public SubmissionApiImpl(
            SubmissionService submissionService,
            NavigationProperties navigationProperties,
            CacheProperties cacheProperties, SubmissionMapper submissionMapper, FeeService feeService) {
        this.submissionService = submissionService;
        this.navigationProperties = navigationProperties;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.feeService = feeService;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {

        logger.info("Generate Url Request Received");

        logger.debug("Attempting to get fee structure for document");
        Fee fee = feeService.getFee(new FeeRequest(generateUrlRequest.getDocumentProperties().getType(), generateUrlRequest.getDocumentProperties().getSubType()));
        logger.info("Successfully retrieved fee [{}]", fee.getAmount());

        //TODO: Replace with a service
        GenerateUrlResponse response = new GenerateUrlResponse();

        response.expiryDate(System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis());

        Optional<Submission> cachedSubmission = submissionService.put(submissionMapper.toSubmission(generateUrlRequest, fee));

        if(!cachedSubmission.isPresent())
            return ResponseEntity.badRequest().body(null);

        response.setEFilingUrl(
                MessageFormat.format(
                               "{0}/{1}",
                                navigationProperties.getBaseUrl(),
                                cachedSubmission.get().getId()));

        logger.debug("{}", response);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GenerateUrlRequest> getConfigurationById(String id) {

        Optional<Submission> submission = submissionService.getByKey(UUID.fromString(id));

        if (!submission.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setNavigation(submission.get().getNavigation());
        generateUrlRequest.setDocumentProperties(submission.get().getDocumentProperties());

        return ResponseEntity.ok(generateUrlRequest);

    }

}
