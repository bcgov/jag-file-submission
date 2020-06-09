package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import ca.bc.gov.open.api.model.Navigation;
import ca.bc.gov.open.api.model.Redirect;
import ca.bc.gov.open.jagefilingapi.cache.RedisStorageService;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class DocumentApiImplTest {
    @InjectMocks
    private DocumentApiImpl sut;

    @Mock
    NavigationProperties navigationProperties;

    @Mock
    RedisStorageService redisStorageService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
    }


    @Test
    @DisplayName("CASE1: when payload is valid")
    public void withValidPayloadShouldReturnOk() {

        when(redisStorageService.put(any())).thenReturn("TEST");

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl("CASE1");
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl("cancel");
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        navigation.setError(errorRedirect);
        generateUrlRequest.setNavigation(navigation);

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals("https://httpbin.org/", actual.getBody().getEFilingUrl());
        assertEquals("TEST", actual.getBody().getStorageId());
        assertNotNull(actual.getBody().getExpiryDate());
    }



}
