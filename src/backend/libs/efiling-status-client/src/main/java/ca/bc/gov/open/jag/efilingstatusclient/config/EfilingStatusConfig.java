package ca.bc.gov.open.jag.efilingstatusclient.config;

import ca.bc.gov.open.jag.efilingstatusclient.CSOStatusServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan
@EnableConfigurationProperties(EfilingStatusProperties.class)
public class EfilingStatusConfig {

    private final EfilingStatusProperties efilingStatusProperties;

    public EfilingStatusConfig(EfilingStatusProperties efilingStatusProperties) {
        this.efilingStatusProperties = efilingStatusProperties;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.ag.csows.filing.status");
        return jaxb2Marshaller;
    }

    @Bean
    public CSOStatusServiceImpl eFilingLookupClient(Jaxb2Marshaller jaxb2Marshaller) {

        CSOStatusServiceImpl eFilingStatusServiceImpl = new CSOStatusServiceImpl();
        eFilingStatusServiceImpl.setDefaultUri(efilingStatusProperties.getFilingStatusSoapUri());
        eFilingStatusServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingStatusServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingStatusServiceImpl;
    }
}
