package ca.bc.gov.open.jagefilingapi.qa.backend.paymentpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.GenerateCardUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PaymentPayload {

    public static final String INTERNAL_CLIENT_NUMBER = "2234";
    public static final String REDIRECT_URL = "http://redirect.com";

    public GenerateCardUrlRequest generateUpdateCard(){
        GenerateCardUrlRequest generateCardUrlRequest = new GenerateCardUrlRequest();
        generateCardUrlRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);
        generateCardUrlRequest.setRedirectUrl(REDIRECT_URL);

        return generateCardUrlRequest;
    }
}
