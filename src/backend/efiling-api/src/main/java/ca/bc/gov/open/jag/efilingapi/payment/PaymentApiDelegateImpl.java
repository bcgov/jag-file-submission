package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.bambora.payment.starter.managment.models.RecurringPaymentDetails;
import ca.bc.gov.open.jag.efilingapi.api.PaymentApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;
import ca.bc.gov.open.jag.efilingapi.error.UrlGenerationException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;

@Service
public class PaymentApiDelegateImpl implements PaymentApiDelegate {

    private static final String URL_GENERATION_FAILURE = "failed to generate bambora card update url.";

    private final BamboraCardService bamboraCardService;

    private final EfilingAccountService efilingAccountService;

    public PaymentApiDelegateImpl(BamboraCardService bamboraCardService, EfilingAccountService efilingAccountService) {
        this.bamboraCardService = bamboraCardService;
        this.efilingAccountService = efilingAccountService;
    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<GenerateCardUrlResponse> updateCreditCard(UUID xTransactionId, GenerateCardUrlRequest generateCardUrlRequest) {

        GenerateCardUrlResponse generateCardUrlResponse = new GenerateCardUrlResponse();
        try {
            generateCardUrlResponse.setBamboraUrl(bamboraCardService.setupRecurringPayment(RecurringPaymentDetails.builder()
                    .orderNumber(MessageFormat.format("C{0}", efilingAccountService.getOrderNumber()))
                    .echoData("customerCode")
                    .redirectURL(generateCardUrlRequest.getRedirectUrl())
                    .endUserId(generateCardUrlRequest.getInternalClientNumber())
                    .create()).toString());
            return ResponseEntity.ok(generateCardUrlResponse);
        } catch (BamboraException e) {
            throw new UrlGenerationException(URL_GENERATION_FAILURE);
        }

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<SetupCardResponse> createCreditCardProfile(UUID xTransactionId,
                                                              String internalClientNumber,
                                                              SetupCardRequest setupCardRequest) {

        SetupCardResponse setupCardResponse = new SetupCardResponse();
        //TODO: Call service to create card, This will be two calls one to bambora then CSO

        setupCardResponse.setResponseCode("1");
        setupCardResponse.setResponseDescription("Operation successful");
        return ResponseEntity.ok(setupCardResponse);

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<SetupCardResponse> updateCreditCardProfile(UUID xTransactionId,
                                                              BigDecimal paymentProfileId,
                                                              String internalClientNumber,
                                                              SetupCardRequest setupCardRequest) {

        SetupCardResponse setupCardResponse = new SetupCardResponse();
        //TODO: Call service to update card, This will be two calls one to bambora then CSO

        setupCardResponse.setResponseCode("1");
        setupCardResponse.setResponseDescription("Operation successful");
        return ResponseEntity.ok(setupCardResponse);

    }

}
