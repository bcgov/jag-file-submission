package ca.bc.gov.open.jag.submission.config;

import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoStatusServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.SoapUtils;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class StatusConfig {

    private final StatusProperties statusProperties;

    public StatusConfig(StatusProperties statusProperties) {
        this.statusProperties = statusProperties;
    }

    @Produces
    public FilingStatusFacadeBean filingStatusFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = new EfilingSoapClientProperties();
        return SoapUtils.getPort(FilingStatusFacadeBean.class, efilingSoapClientProperties, statusProperties.isDebugEnabled());
    }

    @Produces
    private EfilingStatusService efilingStatusService() {
        return new CsoStatusServiceImpl(filingStatusFacadeBean(),new FilePackageMapperImpl());
    }

}
