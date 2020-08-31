package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceSession;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcsostarter.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { DateMapper.class })
public interface ServiceMapper {

    @Mapping(target = "accountId", source = "efilingService.accountId")
    @Mapping(target = "clientId", source = "efilingService.clientId")
    @Mapping(target = "clientReferenceTxt", source = "efilingService.clientReferenceTxt")
    @Mapping(target = "courtFileNo", source = "efilingService.courtFileNumber")
    @Mapping(target = "documentsProcessed", source = "efilingService.documentsProcessed")
    @Mapping(target = "entDtm", source = "efilingService.entryDateTime")
    @Mapping(target = "entUserId", source = "efilingService.clientId")
    @Mapping(target = "serviceId", source = "efilingService.serviceId")
    @Mapping(target = "serviceReceivedDtm", source = "efilingService.serviceReceivedDateTime")
    @Mapping(target = "serviceReceivedDtmText", source = "efilingService.serviceReceivedDtmText")
    @Mapping(target = "serviceSubtypeCd", source = "efilingService.serviceSubtypeCd")
    @Mapping(target = "serviceTypeCd", source = "efilingService.serviceTypeCd")
    @Mapping(target = "serviceTypeDesc", source = "efilingService.serviceTypeDesc")
    @Mapping(target = "styleOfCause", source = "efilingService.styleOfCause")
    @Mapping(target = "transactions", source = "efilingService.transactions")
    @Mapping(target = "feePaidYn", constant = "false")
    @Mapping(target = "userSessionId", source = "serviceSession.userSessionId")
    @Mapping(target = "serviceSessionId", source = "serviceSession.serviceSessionId")
    Service toService(EfilingService efilingService, ServiceSession serviceSession);


    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm", qualifiedByName= {"DateMapper", "toCurrentDateTime"})
    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "serviceReceivedDtm", qualifiedByName= {"DateMapper", "toCurrentDateTime"})
    @Mapping(target = "serviceSubtypeCd", constant = Keys.SERVICE_SUBTYPE_CD)
    @Mapping(target = "serviceTypeCd", constant = Keys.SERVICE_TYPE_CD)
    @Mapping(target = "feePaidYn", constant = "false")
    @Mapping(target = "userSessionId", source = "serviceSession.userSessionId")
    @Mapping(target = "serviceSessionId", source = "serviceSession.serviceSessionId")
    Service  toCreateService(FilingPackage filingPackage, AccountDetails accountDetails, ServiceSession serviceSession);

}
