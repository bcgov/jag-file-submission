package ca.bc.gov.open.jag.efilingapi.payment.service;

import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;
import ca.bc.gov.open.jag.efilingapi.error.AccountException;
import ca.bc.gov.open.jag.efilingapi.error.PaymentException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingPaymentException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentProfileServiceImpl implements PaymentProfileService {

    Logger logger = LoggerFactory.getLogger(PaymentProfileServiceImpl.class);

    private final EfilingAccountService efilingAccountService;

    private final PaymentAdapter bamboraPaymentAdapter;

    public PaymentProfileServiceImpl(EfilingAccountService efilingAccountService, PaymentAdapter bamboraPaymentAdapter) {
        this.efilingAccountService = efilingAccountService;
        this.bamboraPaymentAdapter = bamboraPaymentAdapter;
    }

    @Override
    public SetupCardResponse createPaymentProfile(String universalId, String clientId, SetupCardRequest setupCardRequest) {

        try {

            PaymentProfile paymentProfile = bamboraPaymentAdapter.createProfile(new EfilingPaymentProfile(setupCardRequest.getTokenCode(), setupCardRequest.getName(), null));

            AccountDetails accountDetails = efilingAccountService.getAccountDetails(universalId);
            accountDetails.updateInternalClientNumber(paymentProfile.getId());
            efilingAccountService.updateClient(accountDetails);

            SetupCardResponse setupCardResponse = new SetupCardResponse();
            setupCardResponse.setPaymentProfileId(paymentProfile.getId());
            setupCardResponse.setResponseCode(paymentProfile.getCode().toEngineeringString());
            setupCardResponse.setResponseDescription(paymentProfile.getMessage());
            return setupCardResponse;

        } catch (EfilingAccountServiceException efilingAccountServiceException) {

            logger.error("Error retrieving account info during card setup ", efilingAccountServiceException);
            throw new AccountException(efilingAccountServiceException.getMessage());

        } catch (EfilingPaymentException efilingPaymentException) {

            logger.error("Error during Bambora card setup", efilingPaymentException);
            throw new PaymentException(efilingPaymentException.getMessage());

        }

    }

    @Override
    public SetupCardResponse updatePaymentProfile(String universalId, String paymentProfileId, String clientId, SetupCardRequest setupCardRequest) {

        try {

            PaymentProfile paymentProfile = bamboraPaymentAdapter.updateProfile(new EfilingPaymentProfile(setupCardRequest.getTokenCode(), setupCardRequest.getName(), null));

            AccountDetails accountDetails = efilingAccountService.getAccountDetails(universalId);
            accountDetails.updateInternalClientNumber(paymentProfile.getId());
            efilingAccountService.updateClient(accountDetails);

            SetupCardResponse setupCardResponse = new SetupCardResponse();
            setupCardResponse.setPaymentProfileId(paymentProfile.getId());
            setupCardResponse.setResponseCode(paymentProfile.getCode().toEngineeringString());
            setupCardResponse.setResponseDescription(paymentProfile.getMessage());
            return setupCardResponse;

        } catch (EfilingAccountServiceException efilingAccountServiceException) {

            logger.error("Error retrieving account info during card setup ", efilingAccountServiceException);
            throw new AccountException(efilingAccountServiceException.getMessage());

        } catch (EfilingPaymentException efilingPaymentException) {

            logger.error("Error during Bambora card setup", efilingPaymentException);
            throw new PaymentException(efilingPaymentException.getMessage());

        }


    }

}
