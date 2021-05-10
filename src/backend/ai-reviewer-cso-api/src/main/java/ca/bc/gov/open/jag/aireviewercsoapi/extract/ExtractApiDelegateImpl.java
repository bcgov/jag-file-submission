package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import ca.bc.gov.open.jag.aireviewercsoapi.api.ExtractApiDelegate;
import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtracNotification;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ExtractApiDelegateImpl implements ExtractApiDelegate {

    @Override
    public ResponseEntity<Void> extractNotification(UUID xTransactionId, ExtracNotification extracNotification) {
        return ResponseEntity.noContent().build();
    }
}
