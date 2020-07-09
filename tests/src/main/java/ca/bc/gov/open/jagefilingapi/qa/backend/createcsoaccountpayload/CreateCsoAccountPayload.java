package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateCsoAccountPayload {

    private static final String STRING = "string";

    public String createCsoAccountPayload() throws JsonProcessingException {
        Accounts accounts = new Accounts(STRING, STRING);
        CsoPayload csoPayload = new CsoPayload(STRING, STRING, STRING, STRING, accounts);

        ObjectMapper csoAccountPayload = new ObjectMapper();
        return csoAccountPayload.writeValueAsString(csoPayload);
    }
}
