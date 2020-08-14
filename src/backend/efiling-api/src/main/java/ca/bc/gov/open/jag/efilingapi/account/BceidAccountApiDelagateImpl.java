package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.BceidAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.BceidAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@Service
public class BceidAccountApiDelagateImpl implements BceidAccountApiDelegate {

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<BceidAccount> getBceidAccount(UUID xTransactionId) {

        BceidAccount result = new BceidAccount();

        result.setFirstName("Bob");
        result.setMiddleName("Alan");
        result.setLastName("Ross");

        return ResponseEntity.ok(result);

    }
}
