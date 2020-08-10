package ca.bc.gov.open.jag.efilingapi.payment;

import org.junit.jupiter.api.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("BamboraPaymentAdapter")
public class BamboraPaymentAdapterTest {
    private static final String APIKEY = "APIKEY";
    public static MockWebServer mockBackEnd;


    private BamboraProperties bamboraProperties;

    BamboraPaymentAdapter sut;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();


    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        bamboraProperties = new BamboraProperties();
        bamboraProperties.setBasePath(String.format("http://localhost:%s",
                mockBackEnd.getPort()));
        bamboraProperties.setApiKey(APIKEY);

        sut = new BamboraPaymentAdapter(bamboraProperties);
    }

    @Test
    @DisplayName("Test Approved")
    public void withValidRequestPaymentIsApproved() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"test\":\"test\"}");
        mockResponse.addHeader("content-type: application/json;");
        mockResponse.setResponseCode(200);
        mockBackEnd.enqueue(mockResponse);
    }

    @Test
    @DisplayName("Test Failed")
    public void withValidRequestPaymentIsFailed() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"test\":\"test\"}");
        mockResponse.addHeader("content-type: application/json;");
        mockResponse.setResponseCode(200);
        mockBackEnd.enqueue(mockResponse);
    }

    @Test
    @DisplayName("Test Exception")
    public void withInValidRequestException() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"test\":\"test\"}");
        mockResponse.addHeader("content-type: application/json;");
        mockResponse.setResponseCode(500);
        mockBackEnd.enqueue(mockResponse);
    }

}
