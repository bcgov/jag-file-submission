package ca.bc.gov.open.jag.efilingworker;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.efilingsubmissionclient.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingworker.service.DocumentStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.File;


@Component
public class EfilingMessageConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DocumentStoreService documentStoreService;

    private final EfilingSubmissionService efilingSubmissionService;

    public EfilingMessageConsumer(DocumentStoreService documentStoreService, EfilingSubmissionService efilingSubmissionService) {
        this.documentStoreService = documentStoreService;
        this.efilingSubmissionService = efilingSubmissionService;
    }

    @RabbitListener(queues = Keys.QUEUE_NAME)
    public void acceptMessage(String guid) {
        logger.info("Message received");
        //TODO: get data from redis?
        logger.info("Getting file");
        //TODO: get file
        logger.info("Uploading file");
        documentStoreService.uploadFile(new File(""));
        logger.info("Submitting");
        efilingSubmissionService.submitFiling(new FilingPackage());
    }
}
