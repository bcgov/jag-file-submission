package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import ca.bc.gov.open.jag.aireviewercsoapi.api.ExtractApiDelegate;
import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtractNotification;
import ca.bc.gov.open.jag.aireviewercsoapi.model.ProcessedDocument;
import ca.bc.gov.open.jag.aireviewercsoapi.service.ProcessedDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ExtractApiDelegateImpl implements ExtractApiDelegate {

    private final Logger logger = LoggerFactory.getLogger(ExtractApiDelegateImpl.class);

    private final ProcessedDocumentService processedDocumentService;

    public ExtractApiDelegateImpl(ProcessedDocumentService processedDocumentService) {
        this.processedDocumentService = processedDocumentService;
    }


    @Override
    public ResponseEntity<Void> extractNotification(UUID xTransactionId, ExtractNotification extractNotification) {

        logger.info("Document notification recieved for {}", xTransactionId);
        try {
            processedDocumentService.processDocument(ProcessedDocument
                    .builder()
                    .documentId(extractNotification.getDocumentId())
                    .returnUri(extractNotification.getReturnUri())
                    .transactionId(xTransactionId)
                    .type(extractNotification.getDocumentType())
                    .create());
        } catch (Exception e) {
            logger.error("Error in process document ", e);
        }
        //We don't inform of failure.
        return ResponseEntity.noContent().build();

    }
}
