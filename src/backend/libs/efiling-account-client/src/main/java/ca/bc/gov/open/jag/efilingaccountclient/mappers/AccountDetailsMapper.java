package ca.bc.gov.open.jag.efilingaccountclient.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bceid.webservices.client.v9.BCeIDAccount;
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
    @Mapping(target = "fileRolePresent", source = "fileRolePresent")
    AccountDetails toAccountDetails(ClientProfile clientProfile, Boolean fileRolePresent);

    @Mapping(target = "accountId", defaultValue = "0")
    @Mapping(target = "clientId", defaultValue = "0")
    @Mapping(target = "firstName", source = "individualIdentity.name.firstname.value")
    @Mapping(target = "lastName", source = "individualIdentity.name.surname.value")
    @Mapping(target = "middleName", source = "individualIdentity.name.middleName.value")
    @Mapping(target = "email", source = "contact.email.value")
    @Mapping(target = "fileRolePresent", defaultValue = "false")
    AccountDetails toAccountDetails(BCeIDAccount bceidAccount);

}
