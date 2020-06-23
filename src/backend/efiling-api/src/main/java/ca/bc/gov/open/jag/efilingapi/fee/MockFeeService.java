package ca.bc.gov.open.jag.efilingapi.fee;

import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.fee.models.FeeRequest;

import java.math.BigDecimal;

public class MockFeeService implements FeeService {

    @Override
    public Fee getFee(FeeRequest feeRequest) {
        return new Fee(new BigDecimal(7));
    }

}
