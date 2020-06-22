package ca.bc.gov.open.jag.efilingapi.fee.models;

import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingapi.fee.MockFeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public FeeService feeService() {
        return new MockFeeService();
    }

}
