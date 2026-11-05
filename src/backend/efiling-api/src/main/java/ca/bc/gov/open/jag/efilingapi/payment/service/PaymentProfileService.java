package ca.bc.gov.open.jag.efilingapi.payment.service;

import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;

public interface PaymentProfileService {

    SetupCardResponse createPaymentProfile(String universalId, String clientId, SetupCardRequest setupCardRequest);
    SetupCardResponse updatePaymentProfile(String universalId, String paymentProfileId, String clientId, SetupCardRequest setupCardRequest);

}
