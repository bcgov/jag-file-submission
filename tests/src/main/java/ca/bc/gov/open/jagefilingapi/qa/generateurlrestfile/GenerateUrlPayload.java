package ca.bc.gov.open.jagefilingapi.qa.generateurlrestfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateUrlPayload {

    public static String generateUrlFinalPayload() throws JsonProcessingException {
        Success success = new Success("string");
        Error error = new Error("string");
        Cancel cancel = new Cancel("string");

        Headers headers = new Headers("string", "string", "string");
        SubmissionAccess submissionAccess = new SubmissionAccess("string","GET", headers );
        DocumentProperties documentProperties = new DocumentProperties("string", "string", submissionAccess);
        Navigation navigation = new Navigation(success,error,cancel);
        Payload payload = new Payload(documentProperties, navigation);

        ObjectMapper objMap = new ObjectMapper();
        return objMap.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
    }
}
