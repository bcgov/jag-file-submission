package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.CardOnFile;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentRequest;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.model.PaymentResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@EnableConfigurationProperties(BamboraProperties.class)
public class BamboraPaymentAdapter {

    Logger logger = LoggerFactory.getLogger(BamboraPaymentAdapter.class);

    private final BamboraProperties bamboraProperties;

    public BamboraPaymentAdapter(BamboraProperties bamboraProperties) {
        this.bamboraProperties = bamboraProperties;
    }

    public EfilingTransaction makePayment(EfilingPayment efilingPayment) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(bamboraProperties.getBasePath());
        apiClient.setApiKey(bamboraProperties.getApiKey());
        PaymentsApi paymentsApi = new PaymentsApi(apiClient);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.amount(efilingPayment.getPaymentAmount().doubleValue());
        CardOnFile cardOnFile = new CardOnFile();
        cardOnFile.setSeriesId(efilingPayment.getClientId());
        paymentRequest.cardOnFile(cardOnFile);

        EfilingTransaction result = new EfilingTransaction();

        try {
            PaymentResponse response = paymentsApi.makePayment(paymentRequest);
            result.setEcommerceTransactionId(BigDecimal.valueOf(response.getMessageId()));
            //Reference: https://dev.na.bambora.com/docs/references/payment_APIs/payment_profile_response_codes/
            //TODO: what are the appropriate codes
            if (response.getApproved() == 1) {
                result.setApprovalCd("Approved");
            } else {
                result.setApprovalCd("Failed");
            }
            return result;
        } catch (ApiException e) {
            logger.error("Bambora payment exception", e);
            throw new EfilingSubmissionServiceException("Bambora payment exception", e.getCause());
        }

    }
}
