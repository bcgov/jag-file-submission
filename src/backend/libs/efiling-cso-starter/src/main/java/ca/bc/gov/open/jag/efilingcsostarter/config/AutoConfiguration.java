package ca.bc.gov.open.jag.efilingcsostarter.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.ceis.Csows;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcsoclient.SoapUtils;
import ca.bc.gov.open.jag.efilingcsoclient.*;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import preview.ca.bc.gov.open.jag.efilingcsoclient.mappers.ClientProfileMapper;
import preview.ca.bc.gov.open.jag.efilingcsoclient.mappers.ClientProfileMapperImpl;


@Configuration
@EnableConfigurationProperties({SoapProperties.class, SpringCsoProperties.class})
public class AutoConfiguration {

    private static final String PREVIEW = "preview";
    private static final String FINAL = "!preview";
    private final SoapProperties soapProperties;

    private final CsoProperties csoProperties;

    public AutoConfiguration(SoapProperties soapProperties, SpringCsoProperties csoProperties) {
        this.soapProperties = soapProperties;
        this.csoProperties = csoProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    @Profile(FINAL)
    public AccountFacadeBean accountFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.ACCOUNT);
        return SoapUtils.getPort(AccountFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled());
    }

    @Bean
    @Profile(PREVIEW)
    public preview.ca.bc.gov.ag.csows.accounts.AccountFacadeBean previewAccountFacadeBear() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.ACCOUNT);
        return SoapUtils.getPort(preview.ca.bc.gov.ag.csows.accounts.AccountFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled());
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.ROLE);
       return SoapUtils.getPort(RoleRegistryPortType.class, efilingSoapClientProperties, csoProperties.isDebugEnabled());
    }

    @Bean
    @Primary
    @Profile(FINAL)
    public FilingStatusFacadeBean filingStatusFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.STATUS);
        return SoapUtils.getPort(FilingStatusFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }


    @Bean
    @Profile(PREVIEW)
    public preview.ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean previewFilingStatusFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.STATUS);
        return SoapUtils.getPort(preview.ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public LookupFacadeBean lookupFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.LOOKUP);
        return SoapUtils.getPort(LookupFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public Csows csows() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.CSOWS);
        return SoapUtils.getPort(Csows.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public FilingFacadeBean filingFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.FILING);
        return SoapUtils.getPort(FilingFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public ServiceFacadeBean serviceFacadeBean() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.SERVICE);
        return SoapUtils.getPort(ServiceFacadeBean.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public ReportService reportService() {
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(Clients.REPORT);
        return SoapUtils.getPort(ReportService.class, efilingSoapClientProperties, csoProperties.isDebugEnabled()); }


    @Bean
    public AccountDetailsMapper accountDetailsMapper() {
        return new AccountDetailsMapperImpl();
    }

    @Bean
    public ServiceMapper serviceMapper() {
        return new ServiceMapperImpl();
    }

    @Bean
    public FilingPackageMapper filingPackageMapper() {
        return new FilingPackageMapperImpl();
    }

    @Bean
    public FinancialTransactionMapper financialTransactionMapper() { return new FinancialTransactionMapperImpl(); }

    @Bean
    public FilePackageMapper filePackageMapper() { return new FilePackageMapperImpl(); }


    @Bean
    public DocumentMapper documentMapper() {
        return new DocumentMapperImpl();
    }

    @Bean
    public CsoPartyMapper csoPartyMapper() { return new CsoPartyMapperImpl(); }

    @Bean
    public PackageAuthorityMapper packageAuthorityMapper() { return new PackageAuthorityMapperImpl(); }

    @Bean
    @Primary
    @Profile(FINAL)
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean,
                                                       RoleRegistryPortType roleRegistryPortType,
                                                       AccountDetailsMapper accountDetailsMapper) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType, accountDetailsMapper);
    }

    @Bean
    @Profile(PREVIEW)
    public ClientProfileMapper clientProfileMapper() {
        return new ClientProfileMapperImpl();
    }

    @Bean
    @Profile(PREVIEW)
    public EfilingAccountService efilingAccountServicePreview(preview.ca.bc.gov.ag.csows.accounts.AccountFacadeBean accountFacadeBeanPreview,
                                                              RoleRegistryPortType roleRegistryPortType,
                                                              AccountDetailsMapper accountDetailsMapper, ClientProfileMapper clientProfileMapper) {
        return new preview.ca.bc.gov.open.jag.efilingcsoclient.CsoAccountServiceImpl(accountFacadeBeanPreview, roleRegistryPortType, accountDetailsMapper, clientProfileMapper);
    }


    @Bean
    public EfilingDocumentService efilingDocumentService(FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @Bean
    public EfilingLookupService efilingLookupService(LookupFacadeBean lookupFacadeBean) {
        return new CsoLookupServiceImpl(lookupFacadeBean);
    }

    @Bean
    public CsoCourtServiceImpl efilingCourtService(Csows csows, FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoCourtServiceImpl(csows, filingStatusFacadeBean);
    }

    @Bean
    @Primary
    @Profile(FINAL)
    public EfilingReviewService efilingReviewService(FilingStatusFacadeBean filingStatusFacadeBean, ReportService reportService, FilingFacadeBean filingFacadeBean, RestTemplate restTemplate) {
        return new CsoReviewServiceImpl(filingStatusFacadeBean, reportService, filingFacadeBean, new FilePackageMapperImpl(), csoProperties, restTemplate);
    }

    @Bean
    @Profile(PREVIEW)
    public EfilingReviewService previewEfilingReviewService(preview.ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean filingStatusFacadeBean, ReportService reportService, FilingFacadeBean filingFacadeBean, RestTemplate restTemplate) {
        return new preview.ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl(filingStatusFacadeBean, reportService, filingFacadeBean, new preview.ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl(), csoProperties, restTemplate);
    }

    @Bean
    public EfilingSubmissionService efilingSubmissionService(FilingFacadeBean filingFacadeBean,
                                                             ServiceFacadeBean serviceFacadeBean,
                                                             ServiceMapper serviceMapper,
                                                             FilingPackageMapper filingPackageMapper,
                                                             FinancialTransactionMapper financialTransactionMapper,
                                                             DocumentMapper documentMapper,
                                                             CsoPartyMapper csoPartyMapper,
                                                             PackageAuthorityMapper packageAuthorityMapper) {

        return new CsoSubmissionServiceImpl(
                filingFacadeBean,
                serviceFacadeBean,
                serviceMapper,
                filingPackageMapper,
                financialTransactionMapper,
                csoProperties,
                documentMapper,
                csoPartyMapper,
                packageAuthorityMapper); }



}
