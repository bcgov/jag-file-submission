package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.CsoAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccount;
import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccountUpdateRequest;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Suite of services to manage cso account
 */
@Service
public class CsoAccountApiDelegateImpl implements CsoAccountApiDelegate {

    private static final String CREATE_ACCOUNT_EXCEPTION = "Error Creating CSO account.";
    private static final String INVALID_UNIVERSAL_ID = "Invalid universal id.";
    private static final String MISSING_IDENTITY_PROVIDER = "identityProviderAlias claim missing in jwt token.";
    private static final String MISSING_UNIVERSAL_ID = "universal-id claim missing in jwt token.";
    private static final String UPDATE_CLIENT_EXCEPTION = "Error Updating CSO client account.";

    Logger logger = LoggerFactory.getLogger(CsoAccountApiDelegateImpl.class);

    private final AccountService accountService;
    private final CsoAccountMapper csoAccountMapper;


    public CsoAccountApiDelegateImpl(AccountService accountService, CsoAccountMapper csoAccountMapper) {
        this.accountService = accountService;
        this.csoAccountMapper = csoAccountMapper;
    }


    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<CsoAccount> createAccount(UUID xTransactionId, CreateCsoAccountRequest createAccountRequest) {
        
        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent())
            throw new MissingUniversalIdException(MISSING_UNIVERSAL_ID);

        Optional<String> identityProvider = SecurityUtils.getIdentityProvider();

        if(!identityProvider.isPresent())
            throw new MissingIdentityProviderException(MISSING_IDENTITY_PROVIDER);

        try {

            logger.debug("attempting to create a cso account");
            
            AccountDetails accountDetails = accountService.createAccount(universalId.get(), identityProvider.get(),createAccountRequest);

            logger.info("Account successfully created");

            return new ResponseEntity<>(csoAccountMapper.toCsoAccount(accountDetails), HttpStatus.CREATED);

        } catch (EfilingAccountServiceException e) {

            logger.error("Exception while trying to create a CSO Account", e);
            throw new CreateAccountException(CREATE_ACCOUNT_EXCEPTION);
        }

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<CsoAccount> getCsoAccount(UUID xTransactionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(HttpStatus.FORBIDDEN);

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId.get());

        if(accountDetails == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(csoAccountMapper.toCsoAccount(accountDetails));

    }


    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<CsoAccount> updateCsoAccount(UUID xTransactionId, CsoAccountUpdateRequest clientUpdateRequest) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent())
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        ResponseEntity response;

        try {

            AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId.get());

            if(accountDetails == null) {
                throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);
            }

            accountDetails.updateInternalClientNumber(clientUpdateRequest.getInternalClientNumber());

            accountService.updateClient(accountDetails);

            response = ResponseEntity.ok(csoAccountMapper.toCsoAccount(accountDetails));
        } catch (EfilingAccountServiceException e) {
            logger.warn(e.getMessage(), e);
            throw new UpdateClientException(UPDATE_CLIENT_EXCEPTION);
        }

        return response;
    }

}
