package ca.bc.gov.open.jagefilingapi.fee;

import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;

import java.math.BigDecimal;

public class MockFeeService implements FeeService {

    @Override
    public Fee getFee(FeeRequest feeRequest) {
        return new Fee(new BigDecimal(7));
    }

}
