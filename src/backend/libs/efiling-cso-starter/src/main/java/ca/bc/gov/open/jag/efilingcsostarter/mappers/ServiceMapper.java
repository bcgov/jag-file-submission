package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceSession;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
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
    @Mapping(target = "feePaidYn", constant = "feePaid")
    @Mapping(target = "userSessionId", source = "serviceSession.userSessionId")
    @Mapping(target = "serviceSessionId", source = "serviceSession.serviceSessionId")
    Service toService(EfilingService efilingService, ServiceSession serviceSession, Boolean feePaid);

    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "clientReferenceTxt", source = "clientReferenceTxt")
    @Mapping(target = "courtFileNumber", source = "courtFileNo")
    @Mapping(target = "documentsProcessed", source = "documentsProcessed")
    @Mapping(target = "entryDateTime", source = "entDtm")
    @Mapping(target = "entryUserId", source = "entUserId")
    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "serviceReceivedDateTime", source = "serviceReceivedDtm")
    @Mapping(target = "serviceReceivedDtmText", source = "serviceReceivedDtmText")
    @Mapping(target = "serviceSessionId", source = "serviceSessionId")
    @Mapping(target = "serviceSubtypeCd", source = "serviceSubtypeCd")
    @Mapping(target = "serviceTypeCd", source = "serviceTypeCd")
    @Mapping(target = "serviceTypeDesc", source = "serviceTypeDesc")
    @Mapping(target = "styleOfCause", source = "styleOfCause")
    EfilingService toEfilingService(Service service);
}
