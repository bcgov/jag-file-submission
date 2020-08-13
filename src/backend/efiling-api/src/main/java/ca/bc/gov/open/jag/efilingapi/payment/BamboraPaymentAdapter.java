package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class BamboraPaymentAdapter {

    Logger logger = LoggerFactory.getLogger(BamboraPaymentAdapter.class);

    private final PaymentsApi paymentsApi;

    public BamboraPaymentAdapter(PaymentsApi paymentsApi) {
        this.paymentsApi = paymentsApi;
    }

    public EfilingTransaction makePayment(EfilingPayment efilingPayment) {

        HashMap<CardPurchaseResponse.CardTypeEnum, String> types = new HashMap<>();

        types.put(CardPurchaseResponse.CardTypeEnum.AM,"AX");
        //This seems to have been consolidated
        types.put(CardPurchaseResponse.CardTypeEnum.NN_VI,"NN'VI");
        types.put(CardPurchaseResponse.CardTypeEnum.MC,"M");
        types.put(CardPurchaseResponse.CardTypeEnum.DI,"DI");
        types.put(CardPurchaseResponse.CardTypeEnum.JB,"JB");

        EfilingTransaction result = new EfilingTransaction();

        try {

            PaymentRequest payload = buildPaymentRequest(efilingPayment);
            PaymentResponse response = paymentsApi.makePayment(payload);
            result.setEcommerceTransactionId(new BigDecimal(response.getId()));
            result.setEntDtm(DateUtils.getCurrentXmlDate());
            result.setInvoiceNo(response.getOrderNumber());
            result.setTransactonDtm(DateUtils.getCurrentXmlDate());
            result.setTransactionAmt(BigDecimal.valueOf(response.getAmount()));
            result.setTransactionStateCd(response.getApproved() == PaymentConstants.BAMBORA_APPROVAL_RESPONSE
                    ? PaymentConstants.TRANSACTION_STATE_APPROVED : PaymentConstants.TRANSACTION_STATE_DECLINED);
            result.setApprovalCd(response.getAuthCode());
            result.setTransactionTypeCd(PaymentConstants.TRANSACTION_TYPE_CD);
            result.setReferenceMessageTxt(response.getMessage());
            result.setTransactionSubtypeCd(PaymentConstants.TRANSACTION_SUB_TYPE_CD);
            result.setCreditCardTypeCd(types.get(response.getCard().getCardType()));
            result.setProcessDt(DateUtils.getXmlDate(response.getCreated()));

            return result;

        } catch (ApiException | DatatypeConfigurationException e) {

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
