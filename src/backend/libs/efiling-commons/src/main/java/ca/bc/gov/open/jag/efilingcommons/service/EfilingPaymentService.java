package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;

public interface EfilingPaymentService {
    EfilingTransaction makePayment(EfilingPayment efilingPayment);
}
