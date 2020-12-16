package ca.bc.gov.open.jag.efilingcommons.payment;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;

public interface PaymentAdapter {

    PaymentTransaction makePayment(EfilingPayment efilingPayment);

}
