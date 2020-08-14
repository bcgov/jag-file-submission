package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.BceidAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.BceidAccount;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class BceidAccountApiDelagateImpl implements BceidAccountApiDelegate {

    @Override
    public ResponseEntity<BceidAccount> getBceidAccount(UUID xTransactionId) {

        BceidAccount result = new BceidAccount();

        result.setFirstName("Bob");
        result.setMiddleName("Alan");
        result.setLastName("Ross");

        return ResponseEntity.ok(result);

    }
}
