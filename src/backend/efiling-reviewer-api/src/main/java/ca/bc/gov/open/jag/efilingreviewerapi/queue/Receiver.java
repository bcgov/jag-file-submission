package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Receiver {
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final Integer waitTime;

    private final DocumentsApiDelegate documentsApiDelegate;

    private final DiligenService diligenService;

    private final StringRedisTemplate stringRedisTemplate;

    public Receiver(Integer waitTime, DocumentsApiDelegate documentsApiDelegate, DiligenService diligenService, StringRedisTemplate stringRedisTemplate) {
        this.waitTime = waitTime;
        this.documentsApiDelegate = documentsApiDelegate;
        this.diligenService = diligenService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void receiveMessage(String event) {
        logger.debug("Message received");

        BigDecimal documentId = new BigDecimal(event);

        DiligenDocumentDetails diligenDocumentDetails = diligenService.getDocumentDetails(documentId);

        DocumentEvent documentEvent = new DocumentEvent();

        documentEvent.setDocumentId(documentId);

        documentEvent.setStatus(diligenDocumentDetails.getFileStatus());

        try {
           logger.info("Calling document event");
           documentsApiDelegate.documentEvent(UUID.randomUUID(), documentEvent);
           if (Keys.ACCEPTED_STATUS.contains(diligenDocumentDetails.getFileStatus())) {
               TimeUnit.SECONDS.sleep(waitTime);
               stringRedisTemplate.convertAndSend("documentWait", documentId.toPlainString());
           } else {
               logger.info("existing on status {}", diligenDocumentDetails.getFileStatus());
           }
        } catch (InterruptedException e) {
            logger.error("Temporary code should have slept for two seconds");
        }
    }

}
