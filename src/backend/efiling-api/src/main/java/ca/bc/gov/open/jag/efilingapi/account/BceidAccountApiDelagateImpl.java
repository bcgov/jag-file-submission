package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.bceid.starter.account.BCeIDAccountService;
import ca.bc.gov.open.bceid.starter.account.GetAccountRequest;
import ca.bc.gov.open.bceid.starter.account.models.IndividualIdentity;
import ca.bc.gov.open.jag.efilingapi.account.mappers.BceidAccountMapper;
import ca.bc.gov.open.jag.efilingapi.api.BceidAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.BceidAccount;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BceidAccountApiDelagateImpl implements BceidAccountApiDelegate {


    private final BCeIDAccountService bCeIDAccountService;
    private final BceidAccountMapper bceidAccountMapper;

    public BceidAccountApiDelagateImpl(BCeIDAccountService bCeIDAccountService, BceidAccountMapper bceidAccountMapper) {
        this.bCeIDAccountService = bCeIDAccountService;
        this.bceidAccountMapper = bceidAccountMapper;
    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<BceidAccount> getBceidAccount(UUID xTransactionId) {

        Optional<String> userId = SecurityUtils.getUniversalIdFromContext();

        if(!userId.isPresent())
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        Optional<IndividualIdentity> individualIdentity = bCeIDAccountService.getIndividualIdentity(
                GetAccountRequest.IndividualSelfRequest(userId.get().toString().replace("-", "").toUpperCase()));

        if(!individualIdentity.isPresent())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(bceidAccountMapper.toBceidAccount(individualIdentity.get()));

    }
}
