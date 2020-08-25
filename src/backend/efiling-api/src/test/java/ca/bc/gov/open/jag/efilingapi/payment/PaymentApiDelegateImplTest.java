package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
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
    private PaymentApiDelegateImpl sut;

    @Mock
    BamboraCardService bamboraCardServiceMock;

    @BeforeAll
    public void setUp() throws MalformedURLException {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(new Uri(REDIRECTED_URL)).when(bamboraCardServiceMock).setupRecurringPayment(
                ArgumentMatchers.argThat(request -> request.getEndUserId().equals(BigDecimal.TEN.toString())));

        Mockito.doThrow(BamboraException.class).when(bamboraCardServiceMock).setupRecurringPayment(
                ArgumentMatchers.argThat(request -> request.getEndUserId().equals(BigDecimal.ONE.toString())));

        sut = new PaymentApiDelegateImpl(bamboraCardServiceMock);
    }

    @Test
    @DisplayName("200: ok url was generated")
    public void withCorrectVariableReturnGeneratedUrl() {
        GenerateCardUrlRequest request = new GenerateCardUrlRequest();
        request.setClientId(BigDecimal.TEN);
        request.setRedirectUrl(REDIRECT_URL);
        ResponseEntity<GenerateCardUrlResponse> actual = sut.updateCreditCard(UUID.randomUUID(),request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(REDIRECTED_URL, actual.getBody().getBamboraUrl());
    }

    @Test
    @DisplayName("500: exception was thrown")
    public void withBamboraThorwsException() {
        GenerateCardUrlRequest request = new GenerateCardUrlRequest();
        request.setClientId(BigDecimal.ONE);
        request.setRedirectUrl(REDIRECT_URL);
        ResponseEntity actual = sut.updateCreditCard(UUID.randomUUID(),request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(URL_GENERATION_FAILURE.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(URL_GENERATION_FAILURE.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }
}
