package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import ca.bc.gov.open.jag.efilingsubmissionclient.EfilingSubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfiguration.class);
    private final CsoSubmissionProperties csoSubmissionProperties;

    public AutoConfiguration(CsoSubmissionProperties csoSubmissionProperties) {

        this.csoSubmissionProperties = csoSubmissionProperties;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EfilingSubmissionService.class})
    public FilingFacade eFilingSubmissionService() {

        FilingFacade filingFacade = null;
        try {

            QName serviceName = new QName("http://filing.csows.ag.gov.bc.ca/", "FilingFacade");
            URL url = new URL(csoSubmissionProperties.getFilingSubmissionSoapUri());
            filingFacade = new FilingFacade(url, serviceName);
        } catch(MalformedURLException e) {

            LOGGER.error("Malformed URL exception :" + e.getMessage());
        }
        return filingFacade;
    }
}
