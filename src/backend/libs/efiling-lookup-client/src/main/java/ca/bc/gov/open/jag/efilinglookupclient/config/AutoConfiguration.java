package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.ag.csows.LookupFacadeItf;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bc.gov.open.jag.efilinglookupclient.CSOLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SoapProperties.class)
public class AutoConfiguration {

    private final SoapProperties soapProperties;

    public AutoConfiguration(SoapProperties soapProperties) { this.soapProperties = soapProperties; }

    @Bean
    public LookupFacadeItf lookupFacadeItf() {
        return getPort(Clients.LOOKUP, LookupFacadeItf.class);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingLookupService.class})
    public EfilingLookupService efilingLookupService(LookupFacadeItf lookupFacadeItf) {

        return new CSOLookupServiceImpl(lookupFacadeItf);
    }

    public <T> T getPort(Clients clients, Class<T> type) {

        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(type);
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(clients);
        jaxWsProxyFactoryBean.setAddress(efilingSoapClientProperties.getUri());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getUserName()))
            jaxWsProxyFactoryBean.setUsername(efilingSoapClientProperties.getUserName());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getPassword()))
            jaxWsProxyFactoryBean.setPassword(efilingSoapClientProperties.getPassword());
        return type.cast(jaxWsProxyFactoryBean.create());
    }

}
