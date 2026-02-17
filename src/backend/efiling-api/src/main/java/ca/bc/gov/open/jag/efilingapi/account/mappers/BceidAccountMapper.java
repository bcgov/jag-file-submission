package ca.bc.gov.open.jag.efilingapi.account.mappers;

import ca.bc.gov.open.bceid.starter.account.models.IndividualIdentity;
import ca.bc.gov.open.jag.efilingapi.api.model.BceidAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BceidAccountMapper {

    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "lastName", source = "name.surname")
    @Mapping(target = "middleName", source = "name.middleName")
    BceidAccount toBceidAccount(IndividualIdentity individualIdentity);

}
