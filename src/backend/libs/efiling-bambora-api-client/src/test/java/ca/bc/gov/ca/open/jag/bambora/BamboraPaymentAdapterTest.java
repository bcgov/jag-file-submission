package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardPurchaseResponse;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.Custom;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingPaymentException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("BamboraPaymentAdapter")
public class BamboraPaymentAdapterTest {


    private static final String AUTH_CODE = "ACODE";
    private static final String ORDER_NUMBER = "TEST";
    private static final String INTERNAL_CLIENT_NUMBER = "INTERNALCLIENT";
    private static final String MESSAGE = "AMESSAGE";
    @Mock
    PaymentsApi paymentsApiMock;

    PaymentAdapter sut;

    @BeforeEach
    void initialize() {

        MockitoAnnotations.openMocks(this);

        sut = new BamboraPaymentAdapter(paymentsApiMock, null);

    }

    @Test
    @DisplayName("Test Approved")
    public void withValidRequestPaymentIsApproved() throws ApiException {

        Mockito.when(paymentsApiMock.makePayment(any())).thenReturn(createPaymentResponse("123","1"));

        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, ORDER_NUMBER, INTERNAL_CLIENT_NUMBER);

        PaymentTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals(AUTH_CODE, efilingTransaction.getApprovalCd());
        Assertions.assertEquals("APP", efilingTransaction.getTransactionStateCd());
        Assertions.assertEquals(BigDecimal.valueOf(1), efilingTransaction.getEcommerceTransactionId());
        Assertions.assertEquals(BigDecimal.valueOf(10.00), efilingTransaction.getTransactionAmt());
        Assertions.assertEquals(ORDER_NUMBER, efilingTransaction.getInvoiceNo());
        Assertions.assertEquals("AX", efilingTransaction.getCreditCardTypeCd());
        Assertions.assertEquals(MESSAGE, efilingTransaction.getReferenceMessageTxt());
        Assertions.assertNotNull(efilingTransaction.getEntDtm());
        Assertions.assertNotNull(efilingTransaction.getTransactonDtm());
        Assertions.assertNotNull(efilingTransaction.getProcessDt());

    }

    @Test
    @DisplayName("Test Failed")
    public void withValidRequestPaymentIsFailed() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenReturn(createPaymentResponse("123","2"));
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, ORDER_NUMBER, INTERNAL_CLIENT_NUMBER);

        PaymentTransaction efilingTransaction = sut.makePayment(payment);
        Assertions.assertEquals(AUTH_CODE, efilingTransaction.getApprovalCd());
        Assertions.assertEquals("DEC", efilingTransaction.getTransactionStateCd());
        Assertions.assertEquals(BigDecimal.valueOf(1), efilingTransaction.getEcommerceTransactionId());
        Assertions.assertEquals(BigDecimal.valueOf(10.00), efilingTransaction.getTransactionAmt());
        Assertions.assertEquals("AX", efilingTransaction.getCreditCardTypeCd());
        Assertions.assertEquals(MESSAGE, efilingTransaction.getReferenceMessageTxt());
        Assertions.assertNotNull(efilingTransaction.getEntDtm());
        Assertions.assertNotNull(efilingTransaction.getTransactonDtm());
        Assertions.assertNotNull(efilingTransaction.getProcessDt());

    }

    @Test
    @DisplayName("Test Exception")
    public void withInValidRequestException() throws ApiException {
        Mockito.when(paymentsApiMock.makePayment(any())).thenThrow(ApiException.class);
        EfilingPayment payment = new EfilingPayment(BigDecimal.TEN, BigDecimal.TEN, ORDER_NUMBER, INTERNAL_CLIENT_NUMBER);

        Assertions.assertThrows(EfilingPaymentException.class, () -> sut.makePayment(payment));
    }

    private PaymentResponse createPaymentResponse(String messageId, String approved) {

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId("1");
        paymentResponse.setApproved(approved);
        paymentResponse.setMessageId(messageId);
        paymentResponse.setMessage(MESSAGE);
        paymentResponse.setAmount(10.00);
        paymentResponse.setAuthCode(AUTH_CODE);
        paymentResponse.setOrderNumber(ORDER_NUMBER);
        CardPurchaseResponse card = new CardPurchaseResponse();
        card.setCardType(CardPurchaseResponse.CardTypeEnum.AM);
        paymentResponse.setCard(card);
        paymentResponse.setCreated(DateTime.now());

        Custom custom = new Custom();

        paymentResponse.setCustom(custom);
        return paymentResponse;

    }
}
