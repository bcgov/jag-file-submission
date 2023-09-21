package ca.bc.gov.open.jag.efilingapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;

@ControllerAdvice
public class ExceptionControllerAdvisor {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Object> handleAccountException(AccountException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CacheException.class)
    public ResponseEntity<Object> handleCacheException(CacheException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CourtLocationException.class)
    public ResponseEntity<Object> handleCourtLocationException(CourtLocationException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CreateAccountException.class)
    public ResponseEntity<Object> handleCreateAccountException(CreateAccountException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteDocumentException.class)
    public ResponseEntity<Object> handleDeleteDocumentException(DeleteDocumentException ex) {
        return new ResponseEntity<>(getEfilingError(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(DocumentRequiredException.class)
    public ResponseEntity<Object> handleDocumentRequiredException(DocumentRequiredException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentStorageException.class)
    public ResponseEntity<Object> handleDocumentStorageException(DocumentStorageException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(DocumentTypeException.class)
    public ResponseEntity<Object> handleDocumentTypeException(DocumentTypeException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileTypeException.class)
    public ResponseEntity<Object> handleFileTypeException(FileTypeException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInitialSubmissionPayloadException.class)
    public ResponseEntity<Object> handleInvalidInitialSubmissionPayloadException(InvalidInitialSubmissionPayloadException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> handleInvalidRoleException(InvalidRoleException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidUniversalException.class)
    public ResponseEntity<Object> handleInvalidUniversalException(InvalidUniversalException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingApplicationCodeException.class)
    public ResponseEntity<Object> handleMissingApplicationCodeException(MissingApplicationCodeException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingIdentityProviderException.class)
    public ResponseEntity<Object> handleMissingIdentityProviderException(MissingIdentityProviderException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingUniversalIdException.class)
    public ResponseEntity<Object> handleMissingUniversalIdException(MissingUniversalIdException ex) {
        return new ResponseEntity<>(getEfilingError(ex) , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Object> handlePaymentException(PaymentException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SubmissionException.class)
    public ResponseEntity<Object> handleSubmissionException(SubmissionException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateClientException.class)
    public ResponseEntity<Object> handleUpdateClientException(UpdateClientException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UrlGenerationException.class)
    public ResponseEntity<Object> handleUrlGenerationException(UrlGenerationException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FilingPackageNotFoundException.class)
    public ResponseEntity<Object> handleFilingPackageNotFoundException(FilingPackageNotFoundException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoRegistryNoticeException.class)
    public ResponseEntity<Object> handleNoRegistryNoticeException(NoRegistryNoticeException ex) {
        return new ResponseEntity<>(getEfilingError(ex), HttpStatus.BAD_REQUEST);
    }

    private EfilingError getEfilingError(EfilingException ex) {
        EfilingError efilingError = new EfilingError();
        efilingError.setError(ex.getErrorCode());
        efilingError.setMessage(ex.getMessage());

        if (ex instanceof InvalidInitialSubmissionPayloadException) {
            efilingError.setDetails(((InvalidInitialSubmissionPayloadException) ex).getDetails());
        }
        return efilingError;
    }
}
