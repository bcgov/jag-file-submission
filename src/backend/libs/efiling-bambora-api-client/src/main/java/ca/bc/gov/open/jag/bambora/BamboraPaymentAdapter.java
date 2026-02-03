package ca.bc.gov.open.jag.bambora;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.ProfilesApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingPaymentException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentProfile;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class BamboraPaymentAdapter implements PaymentAdapter {

    Logger logger = LoggerFactory.getLogger(BamboraPaymentAdapter.class);

    private final PaymentsApi paymentsApi;

    private final ProfilesApi profilesApi;

    public BamboraPaymentAdapter(PaymentsApi paymentsApi, ProfilesApi profilesApi) {
        this.paymentsApi = paymentsApi;
        this.profilesApi = profilesApi;
    }

    public PaymentTransaction makePayment(EfilingPayment efilingPayment) {

        PaymentTransaction result = new PaymentTransaction();

        logger.info("Making payment call");

        try {

            PaymentRequest payload = buildPaymentRequest(efilingPayment);
            PaymentResponse response = paymentsApi.makePayment(payload);
            result.setEcommerceTransactionId(new BigDecimal(response.getId()));
            result.setEntDtm(DateTime.now());
            result.setInvoiceNo(response.getOrderNumber());
            result.setTransactonDtm(DateTime.now());
            result.setTransactionAmt(BigDecimal.valueOf(response.getAmount()));
            if (response.getApproved().equals(PaymentConstants.BAMBORA_APPROVAL_RESPONSE)) {
                MDC.put(PaymentConstants.MDC_EFILING_SUBMISSION_FEE, response.getAmount().toString());
                logger.info("Successful payment of [{}]", response.getAmount());
                MDC.remove(PaymentConstants.MDC_EFILING_SUBMISSION_FEE);
                result.setTransactionStateCd(PaymentConstants.TRANSACTION_STATE_APPROVED);
            } else {
                logger.info("Failed payment");
                result.setTransactionStateCd(PaymentConstants.TRANSACTION_STATE_DECLINED);
            }
            result.setApprovalCd(response.getAuthCode());
            result.setReferenceMessageTxt(response.getMessage());
            result.setCreditCardTypeCd(PaymentConstants.CARD_TYPES.get(response.getCard().getCardType()));
            result.setProcessDt(response.getCreated());
            result.setInternalClientNo(response.getCustom().getRef1());

            return result;

        } catch (ApiException e) {

            logger.error("Bambora payment exception", e);
            throw new EfilingPaymentException("Bambora payment exception", e.getCause());

        }

    }

    @Override
    public PaymentProfile createProfile(EfilingPaymentProfile efilingPaymentProfile) {

        try {

            ProfileBody createProfileBody = new ProfileBody();
            createProfileBody.setValidate(true);
            createProfileBody.setLanguage(PaymentConstants.BAMBORA_LANGUAGE);
            ProfileFromToken profileFromToken = new ProfileFromToken();
            profileFromToken.setCode(efilingPaymentProfile.getCode());
            profileFromToken.setName(efilingPaymentProfile.getName());
            createProfileBody.setToken(profileFromToken);
            ProfileResponse response = profilesApi.createProfile(createProfileBody);

            return new PaymentProfile(response.getCode(), response.getValidation().getApproved(), response.getMessage(), response.getValidation().getId());

        } catch (ApiException e) {

            logger.error("Bambora create payment profile exception", e);
            throw new EfilingPaymentException(MessageFormat.format("Card setup error: {0}", e.getResponseBody()), e.getCause());

        }

    }

    @Override
    public PaymentProfile updateProfile(EfilingPaymentProfile efilingPaymentProfile) {

        try {

            ProfileBody createProfileBody = new ProfileBody();
            createProfileBody.setValidate(true);
            createProfileBody.setLanguage(PaymentConstants.BAMBORA_LANGUAGE);
            ProfileFromToken profileFromToken = new ProfileFromToken();
            profileFromToken.setCode(efilingPaymentProfile.getCode());
            profileFromToken.setName(efilingPaymentProfile.getName());
            createProfileBody.setToken(profileFromToken);
            ProfileResponse response = profilesApi.updateProfile(efilingPaymentProfile.getProfileId(), createProfileBody);

            return new PaymentProfile(response.getCode(), response.getValidation().getApproved(), response.getMessage(), response.getValidation().getId());

        } catch (ApiException e) {

            logger.error("Bambora update payment profile exception", e);
            throw new EfilingPaymentException(MessageFormat.format("Card setup error: {0}", e.getResponseBody()), e.getCause());

        }

    }


    private PaymentRequest buildPaymentRequest(EfilingPayment efilingPayment) {

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setPaymentMethod(PaymentRequest.PaymentMethodEnum.PAYMENT_PROFILE);
        paymentRequest.amount(efilingPayment.getPaymentAmount().doubleValue());
        paymentRequest.setOrderNumber(efilingPayment.getInvoiceNumber());

        Custom customReference = new Custom();
        customReference.setRef1(efilingPayment.getInternalClientNumber());
        customReference.setRef2(PaymentConstants.COURT_SERVICES);
        customReference.setRef3(efilingPayment.getServiceId().toString());
        paymentRequest.setCustom(customReference);

        ProfilePurchase profilePurchase = new ProfilePurchase();
        profilePurchase.setCustomerCode(efilingPayment.getInternalClientNumber());
        profilePurchase.setCardId(PaymentConstants.CARD_ID);
        profilePurchase.setComplete(true);
        paymentRequest.setPaymentProfile(profilePurchase);
        return paymentRequest;

    }
}
