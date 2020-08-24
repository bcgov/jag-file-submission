package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.bambora.payment.starter.managment.models.RecurringPaymentDetails;
import ca.bc.gov.open.jag.efilingapi.api.PaymentApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

public class PaymentApiDelegateImpl implements PaymentApiDelegate {

    private final BamboraCardService bamboraCardService;

    public PaymentApiDelegateImpl(BamboraCardService bamboraCardService) {
        this.bamboraCardService = bamboraCardService;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<GenerateCardUrlResponse> updateCreditCard(UUID xTransactionId, GenerateCardUrlRequest generateCardUrlRequest) {

        GenerateCardUrlResponse generateCardUrlResponse = new GenerateCardUrlResponse();
        try {
            generateCardUrlResponse.setBamboraUrl(bamboraCardService.setupRecurringPayment(RecurringPaymentDetails.builder()
                    .orderNumber("")
                    .echoData("")
                    .redirectURL(generateCardUrlRequest.getRedirectUrl())
                    .endUserId(generateCardUrlRequest.getClientId().toString())
                    .create()).toString());
            return ResponseEntity.ok(generateCardUrlResponse);
        } catch (BamboraException e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.URL_GENERATION_FAILURE).create(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
