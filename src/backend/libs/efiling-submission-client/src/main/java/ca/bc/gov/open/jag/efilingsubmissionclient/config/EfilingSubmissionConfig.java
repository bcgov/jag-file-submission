package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import ca.bc.gov.open.jag.efilingsubmissionclient.CSOSubmissionServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@ComponentScan
@EnableConfigurationProperties(EfilingSubmissionProperties.class)
public class EfilingSubmissionConfig {

    private final EfilingSubmissionProperties efilingSubmissionProperties;

    public EfilingSubmissionConfig(EfilingSubmissionProperties efilingSubmissionProperties) {
        this.efilingSubmissionProperties = efilingSubmissionProperties;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.open.jag.ag.csows.filing");
        return jaxb2Marshaller;
    }

    @Bean
    public CSOSubmissionServiceImpl eFilingFacadeClient(Jaxb2Marshaller jaxb2Marshaller) {

        CSOSubmissionServiceImpl eFilingSubmissionServiceImpl = new CSOSubmissionServiceImpl();
        eFilingSubmissionServiceImpl.setDefaultUri(efilingSubmissionProperties.getFilingSubmissionSoapUri());
        eFilingSubmissionServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingSubmissionServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingSubmissionServiceImpl;
    }
}
