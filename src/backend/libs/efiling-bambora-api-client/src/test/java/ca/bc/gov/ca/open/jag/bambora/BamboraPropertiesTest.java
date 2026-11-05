package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.BamboraProperties;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import java.text.MessageFormat;
import java.util.Base64;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("BamboraProperties")
public class BamboraPropertiesTest {

    private static final String PASSCODE = "AVALUE";
    private static final String MERCHANT_ID = "AVALUE";

    @Test
    public void testProperties() {
        BamboraProperties bamboraProperties = new BamboraProperties();
        bamboraProperties.setMerchantId(MERCHANT_ID);
        bamboraProperties.setApiPasscode(PASSCODE);
        Assertions.assertEquals(MERCHANT_ID, bamboraProperties.getMerchantId());
        Assertions.assertEquals(PASSCODE, bamboraProperties.getApiPasscode());
    }

    @Test
    public void testEncode() {
        BamboraProperties bamboraProperties = new BamboraProperties();
        bamboraProperties.setMerchantId(MERCHANT_ID);
        bamboraProperties.setApiPasscode(PASSCODE);

        String result = new String(Base64.getDecoder().decode(bamboraProperties.getEncodedKey()));
        Assertions.assertEquals(MessageFormat.format("{0}:{1}", MERCHANT_ID, PASSCODE), result);
    }
}
