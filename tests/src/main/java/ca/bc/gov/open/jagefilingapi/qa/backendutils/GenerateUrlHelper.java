package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;

public class GenerateUrlHelper {

    private static final String SUBMISSION_ID = "submissionId";
    private static final String GENERATE_URL_PATH_PARAM = "/generateUrl";
    private static final String FILE_NAME_PATH = "/test-document.pdf";

    public String getGeneratedUrl() throws IOException {
        GenerateUrlRequestBuilders generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        Response response = generateUrlRequestBuilders.requestWithSinglePdfDocument(APIResources.valueOf("DOCUMENT_SUBMISSION").toString(), validExistingCSOGuid, FILE_NAME_PATH);
        String submissionId = TestUtil.getJsonPath(response, SUBMISSION_ID);
        response = generateUrlRequestBuilders.postRequestWithPayload(APIResources.valueOf("GENERATE_URL_API").toString(), validExistingCSOGuid, submissionId, GENERATE_URL_PATH_PARAM);

        JsonPath jsonPath = new JsonPath(response.asString());

         return jsonPath.get("efilingUrl");
    }
}
