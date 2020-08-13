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

    private static final String COURT_SERVICES = "COURT SERVICES";
    private static final int CARD_ID = 1;
    private static final String DECLINED = "DEC";
    private static final String APPROVED = "APP";

    public BamboraPaymentAdapter(PaymentsApi paymentsApi) {
        this.paymentsApi = paymentsApi;
    }

    public EfilingTransaction makePayment(EfilingPayment efilingPayment) {

        HashMap<CardPurchaseResponse.CardTypeEnum, String> types = new HashMap<>();

        types.put(CardPurchaseResponse.CardTypeEnum.AM,"AX");
        types.put(CardPurchaseResponse.CardTypeEnum.NN,"NN");
        types.put(CardPurchaseResponse.CardTypeEnum.MC,"M");
        types.put(CardPurchaseResponse.CardTypeEnum.VI,"VI");
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
            result.setTransactionStateCd(response.getApproved() == CARD_ID ? APPROVED : DECLINED);
            result.setApprovalCd(response.getAuthCode());
            result.setTransactionTypeCd("12");
            result.setReferenceMessageTxt(response.getMessage());
            result.setTransactionSubtypeCd("BNST");
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
