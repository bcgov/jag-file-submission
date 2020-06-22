package ca.bc.gov.open.jagefilingapi.fee;

import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;

/**
 * A FeeService interface that provides services
 */
public interface FeeService {

    Fee getFee(FeeRequest feeRequest);

}
