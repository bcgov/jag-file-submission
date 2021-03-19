package ca.bc.gov.open.jag.efilingreviewerapi.error;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AiReviewerControllerAdvisor {

    @ExceptionHandler(DiligenDocumentException.class)
    public ResponseEntity<Object> handleDiligenDocumentException(DiligenDocumentException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DiligenAuthenticationException.class)
    public ResponseEntity<Object> handleDiligenAuthenticationException(DiligenAuthenticationException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AiReviewerDocumentException.class)
    public ResponseEntity<Object> handleAiReviewerDocumentException(AiReviewerDocumentException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AiReviewerVirusFoundException.class)
    public ResponseEntity<Object> handleDocumentExtractVirusFoundException(AiReviewerVirusFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_GATEWAY);
    }
  
    @ExceptionHandler(AiReviewerCacheException.class)
    public ResponseEntity<Object> handleAiReviewerCacheException(AiReviewerCacheException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AiReviewerDocumentTypeMismatchException.class)
    public ResponseEntity<Object> handleDocumentMismatchException(AiReviewerDocumentTypeMismatchException ex, WebRequest request) {
        //TODO: add email to? when this exception is thrown
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
