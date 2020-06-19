package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.open.jag.efilinglookupclient.CSOLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.DemoLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableConfigurationProperties(CSOLookupProperties.class)
public class AutoConfiguration {

    private final CSOLookupProperties csoLookupProperties;

    public AutoConfiguration(CSOLookupProperties csoLookupProperties) {
        this.csoLookupProperties = csoLookupProperties;
    }

    @Bean(name = "CSOLookupMarshaller")
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.ag.csows.lookups");
        return jaxb2Marshaller;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EfilingLookupService.class})
    public EfilingLookupService eFilingLookupService(@Qualifier("CSOLookupMarshaller") Jaxb2Marshaller jaxb2Marshaller) {

        EfilingLookupService eFilingLookupService = new DemoLookupServiceImpl();
//        eFilingLookupService.setDefaultUri(csoLookupProperties.getFilingLookupSoapUri());
//        eFilingLookupService.setMarshaller(jaxb2Marshaller);
//        eFilingLookupService.setUnmarshaller(jaxb2Marshaller);
        return eFilingLookupService;
    }
}
