package ca.bc.gov.open.jag.submission.config;

import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoStatusServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.SoapUtils;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class StatusConfig {

    private final StatusProperties statusProperties;

    public StatusConfig(StatusProperties statusProperties) {
        this.statusProperties = statusProperties;
    }

    @Produces
    @DefaultBean
    public FilingStatusFacadeBean filingStatusFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = new EfilingSoapClientProperties();
        efilingSoapClientProperties.setUri(statusProperties.getUri());
        efilingSoapClientProperties.setUserName(statusProperties.getUserName());
        efilingSoapClientProperties.setPassword(statusProperties.getPassword());
        return SoapUtils.getPort(FilingStatusFacadeBean.class, efilingSoapClientProperties, statusProperties.isDebugEnabled());
    }

    @Produces
    @DefaultBean
    private EfilingStatusService efilingStatusService() {
        return new CsoStatusServiceImpl(filingStatusFacadeBean());
    }

}
