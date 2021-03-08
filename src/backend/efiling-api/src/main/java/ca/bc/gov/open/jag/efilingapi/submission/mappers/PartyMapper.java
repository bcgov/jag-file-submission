package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingcommons.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PartyMapper {

    @Mapping(target = "roleTypeCd", source="roleType")
    @Mapping(target = "nameTypeCd", constant="CUR")
    Individual toIndividual(ca.bc.gov.open.jag.efilingapi.api.model.Individual individual);

    @Mapping(target = "roleTypeCd", source="roleType")
    @Mapping(target = "nameTypeCd", constant="CUR")
    Organization toOrganization(ca.bc.gov.open.jag.efilingapi.api.model.Organization organization);

}
