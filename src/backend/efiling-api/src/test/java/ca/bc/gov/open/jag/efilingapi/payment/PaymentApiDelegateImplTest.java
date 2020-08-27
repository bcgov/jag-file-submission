package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import com.sun.jndi.toolkit.url.Uri;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.URL_GENERATION_FAILURE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PaymentApiDelegateImpl")
public class PaymentApiDelegateImplTest {

    private static final String REDIRECT_URL = "SOMEURL";
    private static final String REDIRECTED_URL = "http:\\www.google.com\bambora";
    private static final String INTERNAL_CLIENT_NUMBER = "123";
    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "1234";
    private PaymentApiDelegateImpl sut;

    @Mock
    BamboraCardService bamboraCardServiceMock;

    @Mock
    EfilingAccountService efilingAccountServiceMock;

    @BeforeAll
    public void setUp() throws MalformedURLException {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(new Uri(REDIRECTED_URL)).when(bamboraCardServiceMock).setupRecurringPayment(
                ArgumentMatchers.argThat(request -> request.getEndUserId().equals(INTERNAL_CLIENT_NUMBER)));

        Mockito.doThrow(BamboraException.class).when(bamboraCardServiceMock).setupRecurringPayment(
                ArgumentMatchers.argThat(request -> request.getEndUserId().equals(FAIL_INTERNAL_CLIENT_NUMBER)));

        sut = new PaymentApiDelegateImpl(bamboraCardServiceMock, efilingAccountServiceMock);
    }

    @Test
    @DisplayName("200: ok url was generated")
    public void withCorrectVariableReturnGeneratedUrl() {
        GenerateCardUrlRequest request = new GenerateCardUrlRequest();
        request.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);
        request.setRedirectUrl(REDIRECT_URL);
        ResponseEntity<GenerateCardUrlResponse> actual = sut.updateCreditCard(UUID.randomUUID(),request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(REDIRECTED_URL, actual.getBody().getBamboraUrl());
    }

    @Test
    @DisplayName("500: exception was thrown")
    public void withBamboraThorwsException() {
        GenerateCardUrlRequest request = new GenerateCardUrlRequest();
        request.setInternalClientNumber(FAIL_INTERNAL_CLIENT_NUMBER);
        request.setRedirectUrl(REDIRECT_URL);
        ResponseEntity actual = sut.updateCreditCard(UUID.randomUUID(),request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(URL_GENERATION_FAILURE.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(URL_GENERATION_FAILURE.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }
}
