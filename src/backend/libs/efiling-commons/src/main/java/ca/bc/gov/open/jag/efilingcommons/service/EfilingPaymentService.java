package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;

import java.math.BigDecimal;

public interface EfilingPaymentService {
    EfilingTransaction makePayment(BigDecimal clientId, BigDecimal amount);
}
