package ca.bc.gov.open.jag.efilingapi.error;

public class FilingPackageNotFoundException extends EfilingException {
    public FilingPackageNotFoundException(String message) {
        super(message,ErrorCode.FILING_PACKAGE_NOT_FOUND);
    }
}
