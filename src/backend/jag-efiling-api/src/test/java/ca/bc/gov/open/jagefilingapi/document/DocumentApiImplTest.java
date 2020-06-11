package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.model.*;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class DocumentApiImplTest {

    private static final String CASE_1 = "CASE1";
    private static final String CANCEL = "CANCEL";
    private static final String ERROR = "ERROR";
    private static final String TEST = "TEST";
    private static final String TYPE = "TYPE";
    private static final String SUBTYPE = "SUBTYPE";
    private static final String URL = "http://doc.com";
    private static final String HEADER = "HEADER";

    @InjectMocks
    private DocumentApiImpl sut;

    @Mock
    NavigationProperties navigationProperties;

    @Mock
    StorageService<GenerateUrlRequest> redisStorageService;

    @Mock
    FeeService feeService;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
        when(navigationProperties.getExpiryTime()).thenReturn(10);
        when(feeService.getFee(any(FeeRequest.class))).thenReturn(new Fee(BigDecimal.TEN));
    }


    @Test
    @DisplayName("CASE1: when payload is valid")
    public void withValidPayloadShouldReturnOk() {

        when(redisStorageService.put(any())).thenReturn(TEST);

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setType("type");
        documentMetadata.setSubType("subType");
        EndpointAccess endpoint = new EndpointAccess();
        endpoint.setVerb(EndpointAccess.VerbEnum.POST);
        endpoint.setUrl("http://doc");
        endpoint.setHeaders(Collections.singletonMap(HEADER, HEADER));
        documentMetadata.setDocumentAccess(endpoint);
        generateUrlRequest.setDocumentMetadata(documentMetadata);
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

        verify(feeService, times(1)).getFee(any(FeeRequest.class));

    }

    @Test
    @DisplayName("CASE1: with validId return payload")
    public void withValidIdReturnPayload() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        DocumentMetadata documentMetaData = new DocumentMetadata();
        EndpointAccess documentAccess = new EndpointAccess();
        documentAccess.setHeaders(Collections.singletonMap(HEADER, HEADER));
        documentAccess.setUrl(URL);
        documentAccess.setVerb(EndpointAccess.VerbEnum.POST);
        documentMetaData.setDocumentAccess(documentAccess);
        documentMetaData.setSubType(SUBTYPE);
        documentMetaData.setType(TYPE);
        generateUrlRequest.setDocumentMetadata(documentMetaData);
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
        when(redisStorageService.getByKey(TEST, GenerateUrlRequest.class)).thenReturn(generateUrlRequest);

        ResponseEntity<GenerateUrlRequest> actual = sut.getConfigurationById(TEST);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(TYPE, actual.getBody().getDocumentMetadata().getType());
        assertEquals(SUBTYPE, actual.getBody().getDocumentMetadata().getSubType());
        assertEquals(URL, actual.getBody().getDocumentMetadata().getDocumentAccess().getUrl());
        assertEquals(CASE_1, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(CANCEL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(ERROR, actual.getBody().getNavigation().getError().getUrl());
    }

    @Test
    @DisplayName("CASE2: with null redis storage response return NotFound")
    public void withNullRedisStorageResponseReturnNotFound() {

        when(redisStorageService.getByKey(any(), Mockito.any()))
                .thenReturn(null);

        ResponseEntity<GenerateUrlRequest> actual = sut.getConfigurationById(TEST);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

}
