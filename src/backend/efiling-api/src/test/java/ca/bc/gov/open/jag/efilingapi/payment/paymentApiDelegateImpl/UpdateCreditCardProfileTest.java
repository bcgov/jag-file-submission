package ca.bc.gov.open.jag.efilingapi.payment.paymentApiDelegateImpl;

import ca.bc.gov.open.bambora.payment.starter.managment.BamboraCardService;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SetupCardResponse;
import ca.bc.gov.open.jag.efilingapi.payment.PaymentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PaymentApiDelegateImpl")
public class UpdateCreditCardProfileTest {

    private static final String INTERNAL_CLIENT_NUMBER = "123";

    private PaymentApiDelegateImpl sut;

    @Mock
    BamboraCardService bamboraCardServiceMock;

    @Mock
    EfilingAccountService efilingAccountServiceMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        sut = new PaymentApiDelegateImpl(bamboraCardServiceMock, efilingAccountServiceMock);

    }

    @Test
    @DisplayName("200: ok profile was updated")
    public void withCorrectVariableReturnGeneratedUrl() {

        SetupCardRequest setupCardRequest = new SetupCardRequest();

        ResponseEntity<SetupCardResponse> actual = sut.updateCreditCardProfile(UUID.randomUUID(), "132", INTERNAL_CLIENT_NUMBER, setupCardRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("1", actual.getBody().getResponseCode());
        Assertions.assertEquals("Operation successful", actual.getBody().getResponseDescription());

    }

}
