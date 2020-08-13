package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardPurchaseResponse;

import java.util.HashMap;
import java.util.Map;

public class PaymentConstants {
    private  PaymentConstants() {

    }
    public static final String COURT_SERVICES = "COURT SERVICES";
    public static final int CARD_ID = 1;
    public static final int BAMBORA_APPROVAL_RESPONSE = 1;
    public static final String TRANSACTION_STATE_DECLINED = "DEC";
    public static final String TRANSACTION_STATE_APPROVED = "APP";
    public static final String TRANSACTION_TYPE_CD = "12";
    public static final String TRANSACTION_SUB_TYPE_CD = "BNST";
    public static Map<CardPurchaseResponse.CardTypeEnum, String> CARD_TYPES;
    static {
        CARD_TYPES = new HashMap<>();
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.AM,"AX");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.NN,"NN");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.MC,"M");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.VI,"VI");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.DI,"DI");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.JB,"JB");
    }
}
