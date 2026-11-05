package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.PaymentConstants;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardPurchaseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentConstantsTest {
    
    
    @Test
    public void TestMapping() {

       Map<CardPurchaseResponse.CardTypeEnum, String> map = PaymentConstants.CARD_TYPES;
        Assertions.assertEquals("AX", map.get(CardPurchaseResponse.CardTypeEnum.AM));;
        Assertions.assertEquals("NN", map.get(CardPurchaseResponse.CardTypeEnum.NN));;
        Assertions.assertEquals("M", map.get(CardPurchaseResponse.CardTypeEnum.MC));;
        Assertions.assertEquals("V", map.get(CardPurchaseResponse.CardTypeEnum.VI));;
        Assertions.assertEquals( "IO", map.get(CardPurchaseResponse.CardTypeEnum.IO));;
        Assertions.assertEquals( "MD", map.get(CardPurchaseResponse.CardTypeEnum.MD));;
        Assertions.assertEquals( "PV", map.get(CardPurchaseResponse.CardTypeEnum.PV));;
        Assertions.assertEquals("DI", map.get(CardPurchaseResponse.CardTypeEnum.DI));;
        Assertions.assertEquals("JB", map.get(CardPurchaseResponse.CardTypeEnum.JB));;
        
    }
    
    
}
