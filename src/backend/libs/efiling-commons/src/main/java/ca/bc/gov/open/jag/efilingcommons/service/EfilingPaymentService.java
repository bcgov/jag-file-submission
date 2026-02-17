package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;

public interface EfilingPaymentService {
    PaymentTransaction makePayment(EfilingPayment efilingPayment);
}
