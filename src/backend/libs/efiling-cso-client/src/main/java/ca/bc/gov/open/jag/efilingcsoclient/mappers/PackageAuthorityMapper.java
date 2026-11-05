package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.PackageAuthority;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PackageAuthorityMapper {

    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "privilegeCd", constant = Keys.PRIVILEGE_CD)
    PackageAuthority toPackageAuthority(AccountDetails accountDetails);


}
