package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDetailsMapper {

    @Mapping(target = "accountId", source = "clientProfile.accountId")
    @Mapping(target = "clientId", source = "clientProfile.clientId")
    @Mapping(target = "internalClientNumber", source = "clientProfile.client.internalClientNo")
    @Mapping(target = "fileRolePresent", source = "fileRolePresent")
    @Mapping(target = "universalId", source = "universalId")
    @Mapping(target = "cardRegistered", source = "clientProfile.client.registeredCreditCardYnBoolean")
    AccountDetails toAccountDetails(String universalId, ClientProfile clientProfile, Boolean fileRolePresent);

}
