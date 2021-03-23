package ca.bc.gov.open.jag.efilingreviewerapi.error;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ApiError;
import ca.bc.gov.open.jag.jagmailit.api.MailSendApi;
import ca.bc.gov.open.jag.jagmailit.api.handler.ApiException;
import ca.bc.gov.open.jag.jagmailit.api.model.EmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AiReviewerControllerAdvisor {

    private final MailSendApi mailSendApi;

    public AiReviewerControllerAdvisor(MailSendApi mailSendApi) {
        this.mailSendApi = mailSendApi;
    }

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
        //TODO: Does this exception require an email?
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AiReviewerRestrictedDocumentException.class)
    public ResponseEntity<Object> handleRestrictedDocumentException(AiReviewerRestrictedDocumentException ex, WebRequest request) {
        try {
            EmailRequest emailRequest = new EmailRequest();

            mailSendApi.mailSend(null);
        } catch (ApiException e) {

        }
        ApiError apiError = new ApiError();
        apiError.setError(ex.getErrorCode());
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
