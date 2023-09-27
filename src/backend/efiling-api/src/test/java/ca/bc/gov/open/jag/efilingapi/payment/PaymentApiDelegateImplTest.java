package ca.bc.gov.open.jag.efilingapi.payment;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.UrlGenerationException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PaymentApiDelegateImpl")
public class PaymentApiDelegateImplTest {

    private static final String REDIRECT_URL = "SOMEURL";
    private static final String REDIRECTED_URL = "http://www.google.com/bambora";
    private static final String INTERNAL_CLIENT_NUMBER = "123";
    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "1234";
    private PaymentApiDelegateImpl sut;

    @Mock
    BamboraCardService bamboraCardServiceMock;

    @Mock
    EfilingAccountService efilingAccountServiceMock;

    @BeforeAll
    public void setUp() throws MalformedURLException, URISyntaxException {
        MockitoAnnotations.openMocks(this);

        Mockito.doReturn(new URI(REDIRECTED_URL)).when(bamboraCardServiceMock).setupRecurringPayment(
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
    @DisplayName("500: bambora exception caught should throw UrlGenerationException")
    public void withBamboraExceptionCaughtShouldThrowUrlGenerationException() {
        GenerateCardUrlRequest request = new GenerateCardUrlRequest();
        request.setInternalClientNumber(FAIL_INTERNAL_CLIENT_NUMBER);
        request.setRedirectUrl(REDIRECT_URL);

        UrlGenerationException exception = Assertions.assertThrows(UrlGenerationException.class, () -> sut.updateCreditCard(UUID.randomUUID(),request));
        Assertions.assertEquals(ErrorCode.URL_GENERATION_FAILURE.toString(), exception.getErrorCode());
    }
}
