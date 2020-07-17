package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateCsoAccountPayload {

    private static final String STRING = "string";

    private String type;
    private String identifier;

    public String createCsoAccountPayload() throws JsonProcessingException {
        Accounts[] accounts = new Accounts[1];
        accounts[0] = new Accounts("BCEID",STRING);
        CsoPayload csoPayload = new CsoPayload(STRING, STRING, STRING, STRING, accounts);

        ObjectMapper csoAccountPayload = new ObjectMapper();
        return csoAccountPayload.writeValueAsString(csoPayload);
    }

    public String createIncorrectTypeCsoAccountPayload() throws JsonProcessingException {
        Accounts[] accounts = new Accounts[1];
        accounts[0] = new Accounts("BCED",STRING);
        CsoPayload csoPayload = new CsoPayload(STRING, STRING, STRING, STRING, accounts);

        ObjectMapper csoAccountPayload = new ObjectMapper();
        return csoAccountPayload.writeValueAsString(csoPayload);
    }
}
