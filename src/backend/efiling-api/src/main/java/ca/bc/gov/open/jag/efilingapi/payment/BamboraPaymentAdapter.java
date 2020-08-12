package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.Custom;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentRequest;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentResponse;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.ProfilePurchase;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BamboraPaymentAdapter {

    public static final String COURT_SERVICES = "COURT SERVICES";
    public static final int CARD_ID = 1;
    public static final String DECLINED = "DEC";
    public static final String APPROVED = "APP";
    Logger logger = LoggerFactory.getLogger(BamboraPaymentAdapter.class);

    private final PaymentsApi paymentsApi;

    public BamboraPaymentAdapter(PaymentsApi paymentsApi) {
        this.paymentsApi = paymentsApi;
    }

    public EfilingTransaction makePayment(EfilingPayment efilingPayment) {

        EfilingTransaction result = new EfilingTransaction();

        try {

            PaymentResponse response = paymentsApi.makePayment(buildPaymentRequest(efilingPayment));
            result.setEcommerceTransactionId(BigDecimal.valueOf(response.getMessageId()));
            result.setEntDtm(DateUtils.getCurrentXmlDate());
            result.setTransactonDtm(DateUtils.getCurrentXmlDate());
            result.setTransactionAmt(BigDecimal.valueOf(response.getAmount()));
            result.setResponseCd(response.getAuthCode());
            result.setInvoiceNo(response.getOrderNumber());
            result.setApprovalCd(response.getApproved() == CARD_ID ? APPROVED : DECLINED);

            return result;

        } catch (ApiException e) {
            logger.error("Bambora payment exception", e);
            throw new EfilingSubmissionServiceException("Bambora payment exception", e.getCause());
        }

    }

    private PaymentRequest buildPaymentRequest(EfilingPayment efilingPayment) {

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setPaymentMethod(PaymentRequest.PaymentMethodEnum.PAYMENT_PROFILE);
        paymentRequest.amount(efilingPayment.getPaymentAmount().doubleValue());
        paymentRequest.setOrderNumber(efilingPayment.getInvoiceNumber());

        Custom customReference = new Custom();
        customReference.setRef1(efilingPayment.getInternalClientNumber());
        customReference.setRef2(COURT_SERVICES);
        customReference.setRef3(efilingPayment.getServiceId().toString());
        paymentRequest.setCustom(customReference);

        ProfilePurchase profilePurchase = new ProfilePurchase();
        profilePurchase.setCustomerCode(efilingPayment.getInternalClientNumber());
        profilePurchase.setCardId(CARD_ID);
        profilePurchase.setComplete(true);
        paymentRequest.setPaymentProfile(profilePurchase);
        return paymentRequest;

    }
}
