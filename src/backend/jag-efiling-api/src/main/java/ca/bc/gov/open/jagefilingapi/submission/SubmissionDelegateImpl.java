package ca.bc.gov.open.jagefilingapi.submission;

import ca.bc.gov.open.jagefilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jagefilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jagefilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jagefilingapi.api.model.UserDetail;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.fee.FeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jagefilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionDelegateImpl implements SubmissionApiDelegate {

    Logger logger = LoggerFactory.getLogger(SubmissionDelegateImpl.class);

    private final SubmissionService submissionService;

    private final NavigationProperties navigationProperties;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final FeeService feeService;

    public SubmissionDelegateImpl(
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

        logger.warn("Id is modified for testing purpose 0 or 1 is appended to it.");
        response.setEfilingUrl(
                MessageFormat.format(
                               "{0}/{1}{2}",
                                navigationProperties.getBaseUrl(),
                                cachedSubmission.get().getId(), Math.round(Math.random())));

        logger.debug("{}", response);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDetail> getSubmissionUserDetail(String id) {

        logger.warn("Response is mocked and returns true or false depending on the end of the string");

        UserDetail response = new UserDetail();
        response.setCsoAccountExists(StringUtils.endsWith(id, "0"));
        return ResponseEntity.ok(response);

    }

}
