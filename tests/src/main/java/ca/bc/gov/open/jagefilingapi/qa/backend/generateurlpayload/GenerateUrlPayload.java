package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateUrlPayload {
    private static final String STRING = "string";

    public String generateUrlFinalPayload() throws JsonProcessingException {
        Success success = new Success(STRING);
        Error error = new Error(STRING);
        Cancel cancel = new Cancel(STRING);
        Headers headers = new Headers(STRING, STRING, STRING);
        SubmissionAccess submissionAccess = new SubmissionAccess(STRING,"GET", headers );
        DocumentProperties documentProperties = new DocumentProperties("DFCL", STRING, submissionAccess);
        Navigation navigation = new Navigation(success,error,cancel);

        Payload payload = new Payload(documentProperties, navigation);

        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(payload);
    }

    public String getNavigationData() throws JsonProcessingException {
        Success success = new Success(STRING);
        Error error = new Error(STRING);
        Cancel cancel = new Cancel(STRING);
        Navigation navigation = new Navigation(success,error,cancel);

        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(navigation);
    }
}

