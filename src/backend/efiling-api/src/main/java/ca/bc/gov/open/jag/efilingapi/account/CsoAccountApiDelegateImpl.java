package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.CsoAccountApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.utils.SecurityUtils;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.UUID;

/**
 * Suite of services to manage cso account
 */
@Service
public class CsoAccountApiDelegateImpl implements CsoAccountApiDelegate {

    Logger logger = LoggerFactory.getLogger(SubmissionApiDelegateImpl.class);

    private final EfilingAccountService efilingAccountService;
    private final CsoAccountMapper csoAccountMapper;


    public CsoAccountApiDelegateImpl(EfilingAccountService efilingAccountService, CsoAccountMapper csoAccountMapper) {
        this.efilingAccountService = efilingAccountService;
        this.csoAccountMapper = csoAccountMapper;
    }


    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<UserFullDetails> createAccount(UUID xTransactionId, CreateCsoAccountRequest createAccountRequest) {
        
        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        try {

            logger.debug("attempting to create a cso account");
            
            AccountDetails accountDetails = efilingAccountService.createAccount(CreateAccountRequest
                    .builder()
                    .universalId(universalId.get())
                    .firstName(createAccountRequest.getFirstName())
                    .lastName(createAccountRequest.getLastName())
                    .middleName(createAccountRequest.getMiddleName())
                    .email(createAccountRequest.getEmail())
                    .create());

            logger.info("Account successfully created");

            UserFullDetails result = totUserDetails(accountDetails);

            return new ResponseEntity<>(result, HttpStatus.CREATED);

        } catch (EfilingAccountServiceException e) {

            logger.error("Exception while trying to create a CSO Account", e);

            EfilingError response = new EfilingError();
            response.setError(ErrorResponse.CREATE_ACCOUNT_EXCEPTION.getErrorCode());
            response.setMessage(ErrorResponse.CREATE_ACCOUNT_EXCEPTION.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<CsoAccount> getCsoAccount(UUID xTransactionId) {

        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(HttpStatus.FORBIDDEN);

        AccountDetails accountDetails = efilingAccountService.getAccountDetails(universalId.get());

        if(accountDetails == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(csoAccountMapper.toCsoAccount(accountDetails));

    }

    private UserFullDetails totUserDetails(AccountDetails accountDetails) {

        // TODO: replace with mapstruct

        UserFullDetails result = new UserFullDetails();
        result.setUniversalId(accountDetails.getUniversalId());
        Account csoAccount = new Account();
        csoAccount.setType(Account.TypeEnum.CSO);
        csoAccount.setIdentifier(accountDetails.getAccountId().toString());
        result.addAccountsItem(csoAccount);
        return result;
    }

}
