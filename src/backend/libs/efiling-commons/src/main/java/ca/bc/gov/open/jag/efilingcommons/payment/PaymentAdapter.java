package ca.bc.gov.open.jag.efilingcommons.payment;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;

public interface PaymentAdapter {

    PaymentTransaction makePayment(EfilingPayment efilingPayment);
    PaymentProfile createProfile(EfilingPaymentProfile efilingPaymentProfile);
    PaymentProfile updateProfile(EfilingPaymentProfile efilingPaymentProfile);

}
