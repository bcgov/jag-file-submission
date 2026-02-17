package ca.bc.gov.open.jag.bambora;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardPurchaseResponse;

import java.util.HashMap;
import java.util.Map;

public class PaymentConstants {
    private  PaymentConstants() {

    }
    public static final String COURT_SERVICES = "COURT SERVICES";
    public static final Integer CARD_ID = 1;
    public static final String BAMBORA_APPROVAL_RESPONSE = "1";
    public static final String BAMBORA_LANGUAGE = "eng";

    public static final String TRANSACTION_STATE_DECLINED = "DEC";
    public static final String TRANSACTION_STATE_APPROVED = "APP";
    public static Map<CardPurchaseResponse.CardTypeEnum, String> CARD_TYPES;
    static {
        CARD_TYPES = new HashMap<>();
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.AM,"AX");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.NN,"NN");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.MC,"M");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.VI,"V");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.IO, "IO");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.MD, "MD");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.PV, "PV");
        // TODO: this card are not in used, they will fail in CSO
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.DI,"DI");
        CARD_TYPES.put(CardPurchaseResponse.CardTypeEnum.JB,"JB");
    }
    public static final String EFILING_APP = "efiling";
    public static final String MDC_EFILING_SUBMISSION_FEE = EFILING_APP + ".submissionFee";
}
