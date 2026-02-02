package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.bambora.payment.starter.managment.models.RecurringPaymentDetails;
import ca.bc.gov.open.jag.efilingapi.api.PaymentApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.error.UrlGenerationException;
import ca.bc.gov.open.jag.efilingapi.payment.service.PaymentProfileService;
import ca.bc.gov.open.jag.efilingapi.payment.service.PaymentProfileServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.INVALID_UNIVERSAL_ID;
import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_IS_REQUIRED;

@Service
public class PaymentApiDelegateImpl implements PaymentApiDelegate {

    Logger logger = LoggerFactory.getLogger(PaymentApiDelegateImpl.class);

    private static final String URL_GENERATION_FAILURE = "failed to generate bambora card update url.";

    private final BamboraCardService bamboraCardService;

    private final EfilingAccountService efilingAccountService;

    private final PaymentProfileService paymentProfileService;

    public PaymentApiDelegateImpl(BamboraCardService bamboraCardService, EfilingAccountService efilingAccountService, PaymentProfileService paymentProfileService) {
        this.bamboraCardService = bamboraCardService;
        this.efilingAccountService = efilingAccountService;
        this.paymentProfileService = paymentProfileService;
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
                                                              String clientId,
                                                              SetupCardRequest setupCardRequest) {


        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if (universalId.isEmpty()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SetupCardResponse setupCardResponse = paymentProfileService.createPaymentProfile(universalId.get(), clientId, setupCardRequest);

        return ResponseEntity.ok(setupCardResponse);

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<SetupCardResponse> updateCreditCardProfile(UUID xTransactionId,
                                                              String paymentProfileId,
                                                              String clientId,
                                                              SetupCardRequest setupCardRequest) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if (universalId.isEmpty()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SetupCardResponse setupCardResponse = paymentProfileService.updatePaymentProfile(universalId.get(), paymentProfileId, clientId, setupCardRequest);

        return ResponseEntity.ok(setupCardResponse);

    }

}
