package ca.bc.gov.open.jag.efilingapi.error;

public class FileTypeException extends EfilingException {
    public FileTypeException(String message) {
        super(message, ErrorCode.FILE_TYPE_ERROR);
    }
}
