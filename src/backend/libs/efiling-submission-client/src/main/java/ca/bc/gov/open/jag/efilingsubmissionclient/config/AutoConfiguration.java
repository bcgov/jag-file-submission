package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import ca.bc.gov.open.jag.efilingsubmissionclient.EfilingSubmissionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties(CsoSubmissionProperties.class)
public class AutoConfiguration {

    private final CsoSubmissionProperties csoSubmissionProperties;

    public AutoConfiguration(CsoSubmissionProperties csoSubmissionProperties) {

        this.csoSubmissionProperties = csoSubmissionProperties;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EfilingSubmissionService.class})
    public FilingFacade eFilingSubmissionService() throws MalformedURLException {

        QName serviceName = new QName("http://filing.csows.ag.gov.bc.ca/", "FilingFacade");
        URL url = new URL(csoSubmissionProperties.getFilingSubmissionSoapUri());
        return new FilingFacade(url, serviceName);

    }
}
