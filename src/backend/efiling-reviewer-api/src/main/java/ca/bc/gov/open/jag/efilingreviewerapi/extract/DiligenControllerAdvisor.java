package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class DiligenControllerAdvisor {

    @ExceptionHandler(DiligenDocumentException.class)
    public ResponseEntity<Object> handleDiligenDocumentException(DiligenDocumentException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DiligenAuthenticationException.class)
    public ResponseEntity<Object> handleDiligenAuthenticationException(DiligenAuthenticationException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
