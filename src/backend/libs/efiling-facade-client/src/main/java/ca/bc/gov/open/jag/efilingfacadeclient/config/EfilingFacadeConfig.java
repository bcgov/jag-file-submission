package ca.bc.gov.open.jag.efilingfacadeclient.config;

import ca.bc.gov.open.jag.efilingfacadeclient.CSOEfilingServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan
@EnableConfigurationProperties(EfilingFacadeProperties.class)
public class EfilingFacadeConfig {

    private final EfilingFacadeProperties efilingFacadeProperties;

    public EfilingFacadeConfig(EfilingFacadeProperties efilingFacadeProperties) {
        this.efilingFacadeProperties = efilingFacadeProperties;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.open.jag.ag.csows.filing");
        return jaxb2Marshaller;
    }

    @Bean
    public CSOEfilingServiceImpl eFilingFacadeClient(Jaxb2Marshaller jaxb2Marshaller) {

        CSOEfilingServiceImpl eFilingFacadeServiceImpl = new CSOEfilingServiceImpl();
        eFilingFacadeServiceImpl.setDefaultUri(efilingFacadeProperties.getFilingFacadeSoapUri());
        eFilingFacadeServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingFacadeServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingFacadeServiceImpl;
    }
}
