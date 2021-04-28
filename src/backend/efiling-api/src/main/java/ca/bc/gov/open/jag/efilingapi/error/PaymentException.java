package ca.bc.gov.open.jag.efilingapi.error;

public class PaymentException extends EfilingException {

    public PaymentException(String message) {
        super(message, ErrorCode.PAYMENT_FAILURE);
    }
}
