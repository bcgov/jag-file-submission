package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import ca.bc.gov.open.jag.aireviewercsoapi.api.ExtractApiDelegate;
import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtractNotification;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ExtractApiDelegateImpl implements ExtractApiDelegate {

    @Override
    public ResponseEntity<Void> extractNotification(UUID xTransactionId, ExtractNotification extractNotification) {
        return ResponseEntity.noContent().build();
    }
}
