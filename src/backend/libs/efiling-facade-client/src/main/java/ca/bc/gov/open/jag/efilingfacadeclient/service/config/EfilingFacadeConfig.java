package ca.bc.gov.open.jag.efilingfacadeclient.service.config;

import ca.bc.gov.open.jag.efilingfacadeclient.EfilingFacadeClient;
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
    public EfilingFacadeClient eFilingFacadeClient(Jaxb2Marshaller jaxb2Marshaller) {
        EfilingFacadeClient eFilingFacadeClient = new EfilingFacadeClient();
        eFilingFacadeClient.setDefaultUri("http://wsgw.dev.jag.gov.bc.ca:8080/csowsJBOSS/FilingFacade");
        eFilingFacadeClient.setMarshaller(jaxb2Marshaller);
        eFilingFacadeClient.setUnmarshaller(jaxb2Marshaller);
        return eFilingFacadeClient;
    }
}
