package ca.bc.gov.open.jag.efilingapi.error;

import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import java.util.List;

/**
 * Efiling Error Builder
 */
public class EfilingErrorBuilder {

    private EfilingError efilingError;

    protected EfilingErrorBuilder() {
        efilingError = new EfilingError();
    }

    public static EfilingErrorBuilder builder() {
        return new EfilingErrorBuilder();
    }

    public EfilingErrorBuilder errorResponse(ErrorResponse errorResponse) {
        this.efilingError.setError(errorResponse.getErrorCode());
        this.efilingError.setMessage(errorResponse.getErrorMessage());
        return this;
    }

    public EfilingErrorBuilder addDetails(List<String> details) {
        this.efilingError.setDetails(details);
        return this;
    }

    public EfilingError create() {
        return this.efilingError;
    }

}
