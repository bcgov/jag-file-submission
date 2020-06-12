package ca.bc.gov.open.jag.efilingfacadeclient.service.config;

import ca.bc.gov.open.jag.efilingfacadeclient.service.EfilingFacadeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class EfilingFacadeConfig {

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.open.jag.ag.csows.filing");
        return jaxb2Marshaller;
    }

    @Bean
    public EfilingFacadeService eFilingFacadeClient(Jaxb2Marshaller jaxb2Marshaller) {
        EfilingFacadeService eFilingFacadeService = new EfilingFacadeService();
        eFilingFacadeService.setDefaultUri("http://wsgw.dev.jag.gov.bc.ca:8080/csowsJBOSS/FilingFacade");
        eFilingFacadeService.setMarshaller(jaxb2Marshaller);
        eFilingFacadeService.setUnmarshaller(jaxb2Marshaller);
        return eFilingFacadeService;
    }
}
