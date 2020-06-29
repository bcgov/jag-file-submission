package ca.bc.gov.open.jag.efilingaccountclient.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDetailsMapper {

    @Mapping(target = "accountId", source = "clientProfile.accountId")
    @Mapping(target = "clientId", source = "clientProfile.clientId")
    @Mapping(target = "firstName", source = "clientProfile.account.accountManager.givenNm")
    @Mapping(target = "lastName", source = "clientProfile.account.accountManager.surnameNm")
    @Mapping(target = "middleName", source = "clientProfile.account.accountManager.middleNm")
    @Mapping(target = "email", source = "clientProfile.account.accountManager.emailTxt")
    @Mapping(target = "efileRole", source = "efileRole")
    AccountDetails toCsoAccountDetails(ClientProfile clientProfile, Boolean efileRole);

}
