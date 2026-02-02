package ca.bc.gov.open.jag.efilingapi.payment.paymentApiDelegateImpl;

import ca.bc.gov.open.bambora.payment.starter.BamboraException;
import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateCardUrlResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.UrlGenerationException;
import ca.bc.gov.open.jag.efilingapi.payment.PaymentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.payment.service.PaymentProfileService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PaymentApiDelegateImpl")
public class CreateCreditCardProfileTest {

    private static final String INTERNAL_CLIENT_NUMBER = "123";
    private static final String RESPONSE_CODE = "1";
    private static final String RESPONSE_DESCRIPTION = "Operation successful";

    private PaymentApiDelegateImpl sut;

    @Mock
    BamboraCardService bamboraCardServiceMock;

    @Mock
    EfilingAccountService efilingAccountServiceMock;

    @Mock
    PaymentProfileService paymentProfileServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private Jwt jwtMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        sut = new PaymentApiDelegateImpl(bamboraCardServiceMock, efilingAccountServiceMock, paymentProfileServiceMock);

    }

    @Test
    @DisplayName("200: ok profile was created")
    public void withCorrectVariableReturnGeneratedUrl() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        SetupCardRequest setupCardRequest = new SetupCardRequest();

        SetupCardResponse setupCardResponse = new SetupCardResponse();
        setupCardResponse.setResponseCode(RESPONSE_CODE);
        setupCardResponse.setResponseDescription(RESPONSE_DESCRIPTION);
        Mockito.when(paymentProfileServiceMock.createPaymentProfile(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(setupCardResponse);

        ResponseEntity<SetupCardResponse> actual = sut.createCreditCardProfile(UUID.randomUUID(),INTERNAL_CLIENT_NUMBER, setupCardRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(RESPONSE_CODE, actual.getBody().getResponseCode());
        Assertions.assertEquals(RESPONSE_DESCRIPTION, actual.getBody().getResponseDescription());

    }

}
