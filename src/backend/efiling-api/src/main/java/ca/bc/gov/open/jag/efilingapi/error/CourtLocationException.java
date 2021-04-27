package ca.bc.gov.open.jag.efilingapi.error;

public class CourtLocationException extends EfilingException {
    public CourtLocationException(String message) {
        super(message,ErrorCode.COURT_LOCATION_ERROR);
    }
}
