package ca.bc.gov.open.jag.efilingworker;

import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
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
    private final EfilingLookupService efilingLookupService;
    public EfilingMessageConsumer(DocumentStoreService documentStoreService, EfilingLookupService efilingLookupService) {
        this.documentStoreService = documentStoreService;
        this.efilingLookupService = efilingLookupService;
    }

    @RabbitListener(queues = Keys.QUEUE_NAME)
    public void acceptMessage(String guid) {
        logger.info("Message received");
        //TODO: get data from redis?
        logger.info("Getting file");
        //TODO: get file
        logger.info("Uploading file");
        documentStoreService.uploadFile(new File(""));
        logger.info("Submiting");
        //TODO: implement filling submit
    }
}
