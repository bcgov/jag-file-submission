package ca.bc.gov.open.jag.efilingworker;

import ca.bc.gov.open.jag.efilingworker.service.DocumentStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class EfilingMessageConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DocumentStoreService documentStoreService;

    public EfilingMessageConsumer(DocumentStoreService documentStoreService) {
        this.documentStoreService = documentStoreService;
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
    }
}
