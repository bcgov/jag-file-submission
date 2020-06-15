package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.open.jag.efilinglookupclient.CSOLookupServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan
@EnableConfigurationProperties(EfilingLookupProperties.class)
public class EfilingLookupConfig {

    private final EfilingLookupProperties efilingLookupProperties;

    public EfilingLookupConfig(EfilingLookupProperties efilingLookupProperties) {
        this.efilingLookupProperties = efilingLookupProperties;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.ag.csows.lookups");
        return jaxb2Marshaller;
    }

    @Bean
    public CSOLookupServiceImpl eFilingLookupClient(Jaxb2Marshaller jaxb2Marshaller) {

        CSOLookupServiceImpl eFilingLookupServiceImpl = new CSOLookupServiceImpl();
        eFilingLookupServiceImpl.setDefaultUri(efilingLookupProperties.getFilingLookupSoapUri());
        eFilingLookupServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingLookupServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingLookupServiceImpl;
    }
}
