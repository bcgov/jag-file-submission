package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface AccountDetailsMapper {

    @Mapping(target = "accountId", source = "clientProfile.accountId")
    @Mapping(target = "clientId", source = "clientProfile.clientId")
    @Mapping(target = "internalClientNumber", source = "clientProfile.client.internalClientNo")
    @Mapping(target = "fileRolePresent", source = "fileRolePresent")
    @Mapping(target = "universalId", source = "universalId")
    @Mapping(target = "cardRegistered", source = "clientProfile.client.registeredCreditCardYnBoolean")
    AccountDetails toAccountDetails(UUID universalId, ClientProfile clientProfile, Boolean fileRolePresent);

    @Mapping(target = "accountId", defaultValue = "0")
    @Mapping(target = "clientId", defaultValue = "0")
    @Mapping(target = "fileRolePresent", defaultValue = "false")
    @Mapping(target = "universalId", source = "universalId")
    AccountDetails toAccountDetails(UUID universalId);

}
