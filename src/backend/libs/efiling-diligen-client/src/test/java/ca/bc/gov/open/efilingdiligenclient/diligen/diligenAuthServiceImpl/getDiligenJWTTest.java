package ca.bc.gov.open.efilingdiligenclient.diligen.diligenAuthServiceImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthServiceImpl;
import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2001;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2001Data;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenAuthServiceImpl test suite")
public class getDiligenJWTTest {

    public static final String JWT = "IMMAJWT";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String NO_DATA_USERNAME = "NO_DATA_USERNAME";
    public static final String NO_DATA_PASSWORD = "NO_DATA_PASSWORD";
    public static final String FAILURE_USERNAME = "FAILURE_USERNAME";
    public static final String FAILURE_PASSWORD = "FAILURE_PASSWORD";

    private

    DiligenAuthServiceImpl sut;

    @Mock
    AuthenticationApi authenticationApiMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DiligenAuthServiceImpl(authenticationApiMock);

    }

    @Test
    @DisplayName("200: jwt returned ")
    public void withValidCredentialsJWTReturned() throws ApiException {

        InlineResponse2001 response2001 = new InlineResponse2001();
        InlineResponse2001Data response2001Data = new InlineResponse2001Data();
        response2001Data.setJwt(JWT);
        response2001.setData(response2001Data);
        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenReturn(response2001);

        String result = sut.getDiligenJWT(USERNAME, PASSWORD);

        assertEquals(JWT, result);

    }

    @Test
    @DisplayName("401: with invalid credentials not authorized ")
    public void withInvalidCredentialsNotAuthorized() throws ApiException {

        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenThrow(new ApiException());

        Assertions.assertThrows(RuntimeException.class, () -> sut.getDiligenJWT(FAILURE_USERNAME, FAILURE_PASSWORD));

    }

    @Test
    @DisplayName("Error: valid credentials but diligen returns null ")
    public void withValidCredentailsDiligenReturnsNull() throws ApiException {

        InlineResponse2001 noDataResponse2001 = new InlineResponse2001();
        Mockito.when(authenticationApiMock.apiLoginPost(any())).thenReturn(noDataResponse2001);

        Assertions.assertThrows(RuntimeException.class, () -> sut.getDiligenJWT(NO_DATA_USERNAME, NO_DATA_PASSWORD));

    }
}
