package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PartyMapper {

    @Mapping(target = "roleTypeCd", source="roleType")
    @Mapping(target = "nameTypeCd", constant="CUR")
    Party toParty(Individual party);

}
