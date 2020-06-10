package ca.bc.gov.open.jagefilingapi.fee.models;

import java.math.BigDecimal;

/**
 * Represents a fee structure
 */
public class Fee {

    private BigDecimal amount;

    public Fee(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
