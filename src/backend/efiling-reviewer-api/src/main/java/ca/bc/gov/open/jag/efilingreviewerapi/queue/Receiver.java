package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final Integer waitTime;

    private final DocumentsApiDelegate documentsApiDelegate;

    public Receiver(Integer waitTime, DocumentsApiDelegate documentsApiDelegate) {
        this.waitTime = waitTime;
        this.documentsApiDelegate = documentsApiDelegate;
    }

    public void receiveMessage(String event) {
        logger.debug("Message received");
        DocumentEvent documentEvent = new DocumentEvent();

        documentEvent.setDocumentId(new BigDecimal(event));

        try {
           TimeUnit.SECONDS.sleep(waitTime);
           logger.info("Calling document event");
           documentsApiDelegate.documentEvent(UUID.randomUUID(), documentEvent);
        } catch (InterruptedException e) {
            logger.error("Temporary code should have slept for two minutes");
        }
    }

}
