package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.paymentpayload.PaymentPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class PaymentRequestBuilders {

    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String ACCESS_TOKEN = "access_token";

    public Response requestToGenerateUpdateCard(String resourceValue) throws IOException, InterruptedException {
        PaymentPayload paymentPayload = new PaymentPayload();
        GenerateUrlRequestBuilders generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        APIResources resourceGet = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();

        String userToken = frontendTestUtil.getUserJwtToken();

      /*  Response response = generateUrlRequestBuilders.getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);*/

        RequestSpecification request = given().auth().preemptive().oauth2(userToken)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .body(paymentPayload.generateUpdateCard());

        return request.when().post(resourceGet.getResource())
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }
}
