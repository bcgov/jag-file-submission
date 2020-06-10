package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import ca.bc.gov.open.api.model.Navigation;
import ca.bc.gov.open.api.model.Redirect;
import ca.bc.gov.open.jagefilingapi.cache.RedisStorageService;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.fee.FeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class DocumentApiImplTest {
    private static final String CASE_1 = "CASE1";
    private static final String CANCEL = "cancel";
    private static final String ERROR = "error";
    private static final String TEST = "TEST";

    @InjectMocks
    private DocumentApiImpl sut;

    @Mock
    NavigationProperties navigationProperties;

    @Mock
    RedisStorageService redisStorageService;

    @Mock
    FeeService feeService;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
        when(navigationProperties.getExpiryTime()).thenReturn(10);
        when(feeService.getFee(Mockito.any(FeeRequest.class))).thenReturn(new Fee(BigDecimal.TEN));
    }


    @Test
    @DisplayName("CASE1: when payload is valid")
    public void withValidPayloadShouldReturnOk() {

        when(redisStorageService.put(any())).thenReturn(TEST);

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl(CASE_1);
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl(CANCEL);
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        navigation.setError(errorRedirect);
        generateUrlRequest.setNavigation(navigation);

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getEFilingUrl().startsWith("https://httpbin.org/"));
        assertNotNull(actual.getBody().getExpiryDate());

        Mockito.verify(feeService, Mockito.times(1)).getFee(Mockito.any(FeeRequest.class));

    }

    @Test
    @DisplayName("CASE1: with validId return payload")
    public void withValidIdReturnPayload() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl(CASE_1);
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl(CANCEL);
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        errorRedirect.setUrl(ERROR);
        navigation.setError(errorRedirect);
        generateUrlRequest.setNavigation(navigation);
        when(redisStorageService.getByKey(TEST)).thenReturn(generateUrlRequest);

        ResponseEntity<GenerateUrlRequest> actual = sut.getConfigurationById(TEST);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(actual.getBody().getNavigation().getSuccess().getUrl(),CASE_1);
        assertEquals(actual.getBody().getNavigation().getCancel().getUrl(), CANCEL);
        assertEquals(actual.getBody().getNavigation().getError().getUrl(), ERROR);
    }
    @Test
    @DisplayName("CASE2: with null redis storage response return NotFound")
    public void withNullRedisStorageResponseReturnNotFound() {

        when(redisStorageService.getByKey(any())).thenReturn(null);

        ResponseEntity<GenerateUrlRequest> actual = sut.getConfigurationById(TEST);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

}
