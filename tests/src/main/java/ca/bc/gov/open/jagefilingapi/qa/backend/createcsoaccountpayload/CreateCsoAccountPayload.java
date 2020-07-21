package ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CreateCsoAccountPayload {

    private static final String STRING = "string";
    private String validExistingCSOGuid;

    public String createCsoAccountPayload() throws IOException {
        Accounts[] accounts = new Accounts[1];
        accounts[0] = new Accounts("BCEID",STRING);
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        CsoPayload csoPayload = new CsoPayload(validExistingCSOGuid, STRING, STRING, STRING, STRING, accounts);

        ObjectMapper csoAccountPayload = new ObjectMapper();
        return csoAccountPayload.writeValueAsString(csoPayload);
    }

    public String createIncorrectTypeCsoAccountPayload() throws IOException {
        Accounts[] accounts = new Accounts[1];
        accounts[0] = new Accounts("BCED",STRING);
        CsoPayload csoPayload = new CsoPayload(validExistingCSOGuid, STRING, STRING, STRING, STRING, accounts);

        ObjectMapper csoAccountPayload = new ObjectMapper();
        return csoAccountPayload.writeValueAsString(csoPayload);
    }
}
