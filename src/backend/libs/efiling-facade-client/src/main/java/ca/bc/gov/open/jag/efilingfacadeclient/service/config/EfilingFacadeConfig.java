package ca.bc.gov.open.jag.efilingfacadeclient.service.config;

import ca.bc.gov.open.jag.efilingfacadeclient.service.EfilingFacadeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    public EfilingFacadeService eFilingFacadeClient(Jaxb2Marshaller jaxb2Marshaller) {

        EfilingFacadeService eFilingFacadeService = new EfilingFacadeService();
        eFilingFacadeService.setDefaultUri(efilingFacadeProperties.getFilingFacadeSoapUri());
        eFilingFacadeService.setMarshaller(jaxb2Marshaller);
        eFilingFacadeService.setUnmarshaller(jaxb2Marshaller);
        return eFilingFacadeService;
    }
}
