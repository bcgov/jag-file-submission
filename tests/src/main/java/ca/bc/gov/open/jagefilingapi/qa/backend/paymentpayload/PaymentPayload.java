package ca.bc.gov.open.jagefilingapi.qa.backend.paymentpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.GenerateCardUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PaymentPayload {

    public static final String INTERNAL_CLIENT_NUMBER = "2234";
    public static final String REDIRECT_URL = "http://redirect.com";
    private GenerateCardUrlRequest generateCardUrlRequest;

    public String validGenerateUrlPayload() throws IOException {
        generateCardUrlRequest = new GenerateCardUrlRequest();
        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(generateUpdateCard());
    }
    
    public GenerateCardUrlRequest generateUpdateCard(){
        generateCardUrlRequest = new GenerateCardUrlRequest();
        generateCardUrlRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);
        generateCardUrlRequest.setRedirectUrl(REDIRECT_URL);

        return generateCardUrlRequest;
    }
}
