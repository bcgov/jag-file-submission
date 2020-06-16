package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import ca.bc.gov.open.jag.efilingsubmissionclient.CSOSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingsubmissionclient.EfilingSubmissionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableConfigurationProperties(CSOSubmissionProperties.class)
public class AutoConfiguration {

    private final CSOSubmissionProperties CSOSubmissionProperties;

    public AutoConfiguration(CSOSubmissionProperties CSOSubmissionProperties) {
        this.CSOSubmissionProperties = CSOSubmissionProperties;
    }

    @Bean(name = "CSOSubmissionMarshaller")
    public Jaxb2Marshaller marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("ca.bc.gov.open.jag.ag.csows.filing");
        return jaxb2Marshaller;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EfilingSubmissionService.class})
    public EfilingSubmissionService eFilingSubmissionClient(@Qualifier("CSOSubmissionMarshaller") Jaxb2Marshaller jaxb2Marshaller) {

        CSOSubmissionServiceImpl eFilingSubmissionServiceImpl = new CSOSubmissionServiceImpl();
        eFilingSubmissionServiceImpl.setDefaultUri(CSOSubmissionProperties.getFilingSubmissionSoapUri());
        eFilingSubmissionServiceImpl.setMarshaller(jaxb2Marshaller);
        eFilingSubmissionServiceImpl.setUnmarshaller(jaxb2Marshaller);
        return eFilingSubmissionServiceImpl;
    }
}
