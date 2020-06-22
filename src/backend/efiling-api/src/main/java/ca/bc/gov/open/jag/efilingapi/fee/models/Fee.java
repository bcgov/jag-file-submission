package ca.bc.gov.open.jag.efilingapi.fee.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Represents a fee structure
 */
public class Fee {

    private BigDecimal amount;

    @JsonCreator
    public Fee(@JsonProperty("amount") BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
