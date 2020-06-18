package ca.bc.gov.open.jag.efilingstatusclient.config;

import ca.bc.gov.open.jag.efilingstatusclient.CSOStatusServiceImpl;
import ca.bc.gov.open.jag.efilingstatusclient.EfilingStatusService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableConfigurationProperties(CSOStatusProperties.class)
public class AutoConfiguration {

    private final CSOStatusProperties csoStatusProperties;

    public AutoConfiguration(CSOStatusProperties csoStatusProperties) {
        this.csoStatusProperties = csoStatusProperties;
    }

    @Bean(name = "CSOStatusMarshaller")
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.ag.csows.filing.status");
        return jaxb2Marshaller;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EfilingStatusService.class})
    public EfilingStatusService eFilingLookupClient(@Qualifier("CSOStatusMarshaller") Jaxb2Marshaller jaxb2Marshaller) {

        CSOStatusServiceImpl eFilingStatusServiceImpl = new CSOStatusServiceImpl();
        eFilingStatusServiceImpl.setDefaultUri(csoStatusProperties.getFilingStatusSoapUri());
        eFilingStatusServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingStatusServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingStatusServiceImpl;
    }
}
