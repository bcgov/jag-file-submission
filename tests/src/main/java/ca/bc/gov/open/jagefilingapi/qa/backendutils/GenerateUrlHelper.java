package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;

public class GenerateUrlHelper {

    private Response response;
    private static final String SUBMISSION_ID = "submissionId";
    private static final String GENERATE_URL_PATH_PARAM = "/generateUrl";
    private static final String FILE_NAME_PATH = "/test-document.pdf";

    public String getGeneratedUrl() throws IOException {
        GenerateUrlRequestBuilders generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument("/submission/documents", validExistingCSOGuid, FILE_NAME_PATH);
        String submissionId = TestUtil.getJsonPath(response, SUBMISSION_ID);
        response = generateUrlRequestBuilders.postRequestWithPayload("/submission/", validExistingCSOGuid, submissionId, GENERATE_URL_PATH_PARAM);

        JsonPath jsonPath = new JsonPath(response.asString());

         return jsonPath.get("efilingUrl");
    }
}
