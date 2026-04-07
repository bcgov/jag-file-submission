package ca.bc.gov.open.jag.efilingapi.payment;

import ca.bc.gov.open.jag.efilingapi.payment.service.PaymentProfileService;
import ca.bc.gov.open.jag.efilingapi.payment.service.PaymentProfileServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public PaymentProfileService paymentProfileService(EfilingAccountService efilingAccountService, PaymentAdapter bamboraPaymentAdapter) {
        return new PaymentProfileServiceImpl(efilingAccountService, bamboraPaymentAdapter);
    }

}
