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


    private static final String AUTH_CODE = "ACODE";
    private static final String ORDER_NUMBER = "TEST";
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

        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, "Test");

        EfilingTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals("Approved", efilingTransaction.getApprovalCd());
        Assertions.assertEquals(BigDecimal.valueOf(123), efilingTransaction.getEcommerceTransactionId());
        Assertions.assertEquals(BigDecimal.valueOf(10.00), efilingTransaction.getTransactionAmt());
        Assertions.assertEquals(AUTH_CODE, efilingTransaction.getResponseCd());
        Assertions.assertEquals(ORDER_NUMBER, efilingTransaction.getInvoiceNo());
        Assertions.assertNotNull(efilingTransaction.getEntDtm());
        Assertions.assertNotNull(efilingTransaction.getTransactonDtm());
    }

    @Test
    @DisplayName("Test Failed")
    public void withValidRequestPaymentIsFailed() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenReturn(createPaymentResponse(123,2));
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, ORDER_NUMBER);

        EfilingTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals("Failed", efilingTransaction.getApprovalCd());
        Assertions.assertEquals(BigDecimal.valueOf(123), efilingTransaction.getEcommerceTransactionId());
        Assertions.assertEquals(BigDecimal.valueOf(10.00), efilingTransaction.getTransactionAmt());
        Assertions.assertEquals(AUTH_CODE, efilingTransaction.getResponseCd());
        Assertions.assertNotNull(efilingTransaction.getEntDtm());
        Assertions.assertNotNull(efilingTransaction.getTransactonDtm());
    }

    @Test
    @DisplayName("Test Exception")
    public void withInValidRequestException() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenThrow(ApiException.class);
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, ORDER_NUMBER);

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.makePayment(payment));
    }
    private PaymentResponse createPaymentResponse(int messageId, int approved) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setApproved(approved);
        paymentResponse.setMessageId(messageId);
        paymentResponse.setAmount(10.00);
        paymentResponse.setAuthCode(AUTH_CODE);
        paymentResponse.setOrderNumber(ORDER_NUMBER);
        return paymentResponse;
    }
}
