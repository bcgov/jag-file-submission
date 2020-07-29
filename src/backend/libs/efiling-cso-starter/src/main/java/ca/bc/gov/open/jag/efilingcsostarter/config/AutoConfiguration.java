package ca.bc.gov.open.jag.efilingcsostarter.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.ceis.Csows;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bc.gov.open.jag.efilingcommons.soap.service.*;
import ca.bc.gov.open.jag.efilingcsostarter.*;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.AccountDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapper;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapperImpl;
import ca.bceid.webservices.client.v9.BCeIDServiceSoap;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;


@Configuration
@EnableConfigurationProperties(SoapProperties.class)
public class AutoConfiguration {

    private final SoapProperties soapProperties;


    public AutoConfiguration(SoapProperties soapProperties) {
        this.soapProperties = soapProperties;
    }

    @Bean
    public AccountFacadeBean accountFacadeBean() {
        return getPort(Clients.ACCOUNT, AccountFacadeBean.class);
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
       return getPort(Clients.ROLE, RoleRegistryPortType.class);
    }

    @Bean
    public BCeIDServiceSoap bCeIDServiceSoap() { return getPort(Clients.BCEID, BCeIDServiceSoap.class); }

    @Bean
    public FilingStatusFacadeBean filingStatusFacadeBean() { return getPort(Clients.STATUS, FilingStatusFacadeBean.class); }

    @Bean
    public LookupFacadeBean lookupFacadeBean() { return getPort(Clients.LOOKUP, LookupFacadeBean.class); }

    @Bean
    public Csows csows() { return getPort(Clients.CSOWS, Csows.class); }

    @Bean
    public FilingFacadeBean filingFacadeBean() { return getPort(Clients.FILING, FilingFacadeBean.class); }

    @Bean
    public ServiceFacadeBean serviceFacadeBean() { return getPort(Clients.SERVICE, ServiceFacadeBean.class); }

    @Bean
    public AccountDetailsMapper accountDetailsMapper() {
        return new AccountDetailsMapperImpl();
    }

    @Bean
    public ServiceMapper serviceMapper() {
        return new ServiceMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean,
                                                       RoleRegistryPortType roleRegistryPortType,
                                                       BCeIDServiceSoap bCeIDServiceSoap,
                                                       AccountDetailsMapper accountDetailsMapper) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType,
                                         bCeIDServiceSoap, accountDetailsMapper);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingDocumentService.class})
    public EfilingDocumentService efilingDocumentService(FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingLookupService.class})
    public EfilingLookupService efilingLookupService(LookupFacadeBean lookupFacadeBean) {
        return new CsoLookupServiceImpl(lookupFacadeBean);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingCourtService.class})
    public EfilingCourtService efilingCourtService(Csows csows) {
        return new CsoCourtServiceImpl(csows);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingSubmissionService.class})
    public EfilingSubmissionService efilingSubmissionService(FilingFacadeBean filingFacadeBean,
                                                             ServiceFacadeBean serviceFacadeBean,
                                                             ServiceMapper serviceMapper) { return new CsoSubmissionServiceImpl(filingFacadeBean, serviceFacadeBean, serviceMapper); }


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
