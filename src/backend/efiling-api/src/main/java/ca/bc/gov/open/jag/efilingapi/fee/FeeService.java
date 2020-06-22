package ca.bc.gov.open.jag.efilingapi.fee;

import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.fee.models.FeeRequest;

/**
 * A FeeService interface that provides services
 */
public interface FeeService {

    Fee getFee(FeeRequest feeRequest);

}
