package ca.bc.gov.open.jag.efilingapi.error;


import org.springframework.http.HttpStatus;

public class DeleteDocumentException extends EfilingException {

    private final HttpStatus httpStatus;

    public DeleteDocumentException(String message, HttpStatus httpStatus) {
        super(message, ErrorCode.DELETE_DOCUMENT_ERROR);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
