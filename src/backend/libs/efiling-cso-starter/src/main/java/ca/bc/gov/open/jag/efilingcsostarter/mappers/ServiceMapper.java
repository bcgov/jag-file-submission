package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ServiceMapper {
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "clientReferenceTxt", source = "clientReferenceTxt")
    @Mapping(target = "courtFileNo", source = "courtFileNumber")
    @Mapping(target = "documentsProcessed", source = "documentsProcessed")
    @Mapping(target = "entDtm", source = "entryDateTime")
    @Mapping(target = "entUserId", source = "clientId")
    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "serviceReceivedDtm", source = "serviceReceivedDateTime")
    @Mapping(target = "serviceReceivedDtmText", source = "serviceReceivedDtmText")
    @Mapping(target = "serviceSessionId", source = "serviceSessionId")
    @Mapping(target = "serviceSubtypeCd", source = "serviceSubtypeCd")
    @Mapping(target = "serviceTypeCd", source = "serviceTypeCd")
    @Mapping(target = "serviceTypeDesc", source = "serviceTypeDesc")
    @Mapping(target = "styleOfCause", source = "styleOfCause")
    @Mapping(target = "transactions", source = "transactions")
    Service toService(EfilingService efilingService);

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
