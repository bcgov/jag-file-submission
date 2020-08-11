package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("BamboraPaymentAdapter")
public class BamboraPaymentAdapterTest {


    @Mock
    PaymentsApi paymentsApiMock;

    BamboraPaymentAdapter sut;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.initMocks(this);

        sut = new BamboraPaymentAdapter(paymentsApiMock);
    }

    @Test
    @DisplayName("Test Approved")
    public void withValidRequestPaymentIsApproved() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenReturn(createPaymentResponse(123,1));

        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN);

        EfilingTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals("Approved", efilingTransaction.getApprovalCd());
        Assertions.assertEquals(BigDecimal.valueOf(123), efilingTransaction.getEcommerceTransactionId());
    }

    @Test
    @DisplayName("Test Failed")
    public void withValidRequestPaymentIsFailed() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenReturn(createPaymentResponse(123,2));
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN);

        EfilingTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals("Failed", efilingTransaction.getApprovalCd());
        Assertions.assertEquals(BigDecimal.valueOf(123), efilingTransaction.getEcommerceTransactionId());
    }

    @Test
    @DisplayName("Test Exception")
    public void withInValidRequestException() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenThrow(ApiException.class);
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN);

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.makePayment(payment));
    }
    private PaymentResponse createPaymentResponse(int messageId, int approved) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setApproved(approved);
        paymentResponse.setMessageId(messageId);
        return paymentResponse;
    }
}
