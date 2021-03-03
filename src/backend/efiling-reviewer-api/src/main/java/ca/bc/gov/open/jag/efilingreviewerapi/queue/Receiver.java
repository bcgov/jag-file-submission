package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final UUID queueGuid = UUID.fromString("b40e9014-024f-481b-a1b9-a84cb99e9c9d");

    private final DocumentsApiDelegate documentsApiDelegate;

    private final AtomicInteger counter = new AtomicInteger();

    public Receiver(DocumentsApiDelegate documentsApiDelegate) {
        this.documentsApiDelegate = documentsApiDelegate;
    }

    public void receiveMessage(String event) {
        logger.debug("Message received");
        counter.incrementAndGet();
        DocumentEvent documentEvent = new DocumentEvent();

        documentEvent.setDocumentId(new BigDecimal(event));
        documentEvent.setStatus("PROCESSED");
        documentsApiDelegate.documentEvent(queueGuid, documentEvent);
    }

    public int getCount() {
        return counter.get();
    }
}
