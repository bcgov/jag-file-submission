package ca.bc.gov.open.jag.efilingaccountclient.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bceid.webservices.client.v9.BCeIDAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface AccountDetailsMapper {

    @Mapping(target = "accountId", source = "clientProfile.accountId")
    @Mapping(target = "clientId", source = "clientProfile.clientId")
    @Mapping(target = "firstName", source = "clientProfile.account.accountManager.givenNm")
    @Mapping(target = "lastName", source = "clientProfile.account.accountManager.surnameNm")
    @Mapping(target = "middleName", source = "clientProfile.account.accountManager.middleNm")
    @Mapping(target = "email", source = "clientProfile.account.accountManager.emailTxt")
    @Mapping(target = "fileRolePresent", source = "fileRolePresent")
    @Mapping(target = "universalId", source = "universalId")
    AccountDetails toAccountDetails(UUID universalId, ClientProfile clientProfile, Boolean fileRolePresent);

    @Mapping(target = "accountId", defaultValue = "0")
    @Mapping(target = "clientId", defaultValue = "0")
    @Mapping(target = "firstName", source = "bceidAccount.individualIdentity.name.firstname.value")
    @Mapping(target = "lastName", source = "bceidAccount.individualIdentity.name.surname.value")
    @Mapping(target = "middleName", source = "bceidAccount.individualIdentity.name.middleName.value")
    @Mapping(target = "email", source = "bceidAccount.contact.email.value")
    @Mapping(target = "fileRolePresent", defaultValue = "false")
    @Mapping(target = "universalId", source = "universalId")
    AccountDetails toAccountDetails(UUID universalId, BCeIDAccount bceidAccount);

}
