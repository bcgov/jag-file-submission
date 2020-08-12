package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardOnFile;
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

    Logger logger = LoggerFactory.getLogger(BamboraPaymentAdapter.class);

    private final PaymentsApi paymentsApi;

    public BamboraPaymentAdapter(PaymentsApi paymentsApi) {
        this.paymentsApi = paymentsApi;
    }

    public EfilingTransaction makePayment(EfilingPayment efilingPayment) {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.amount(efilingPayment.getPaymentAmount().doubleValue());
        paymentRequest.setOrderNumber(efilingPayment.getInvoiceNumber());

        ProfilePurchase profilePurchase = new ProfilePurchase();
        profilePurchase.setCustomerCode(efilingPayment.getInternalClientNumber());
        profilePurchase.setCardId(1);
        paymentRequest.setPaymentProfile(profilePurchase);


        EfilingTransaction result = new EfilingTransaction();

        try {
            PaymentResponse response = paymentsApi.makePayment(paymentRequest);
            result.setEcommerceTransactionId(BigDecimal.valueOf(response.getMessageId()));
            result.setEntDtm(DateUtils.getCurrentXmlDate());
            result.setTransactonDtm(DateUtils.getCurrentXmlDate());
            result.setTransactionAmt(BigDecimal.valueOf(response.getAmount()));
            result.setResponseCd(response.getAuthCode());
            result.setInvoiceNo(response.getOrderNumber());
            //Reference: https://dev.na.bambora.com/docs/references/payment_APIs/payment_profile_response_codes/
            //TODO: what are the appropriate codes
            if (response.getApproved() == 1) {
                result.setApprovalCd("Approved");
            } else {
                result.setApprovalCd("Failed");
            }
            return result;
        } catch (ApiException e) {
            logger.error("Bambora payment exception", e);
            throw new EfilingSubmissionServiceException("Bambora payment exception", e.getCause());
        }

    }
}
