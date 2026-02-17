package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceSession;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ServiceMapper {

    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "serviceReceivedDtm", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "serviceSubtypeCd", constant = Keys.SERVICE_SUBTYPE_CD)
    @Mapping(target = "serviceTypeCd", constant = Keys.SERVICE_TYPE_CD)
    @Mapping(target = "feePaidYn", constant = "false")
    @Mapping(target = "userSessionId", source = "serviceSession.userSessionId")
    @Mapping(target = "serviceSessionId", source = "serviceSession.serviceSessionId")
    @Mapping(target = "updUserId", ignore = true)
    @Mapping(target = "updDtm", ignore = true)
    Service  toCreateService(FilingPackage filingPackage, AccountDetails accountDetails, ServiceSession serviceSession);

}
