package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingcommons.model.Party;
import org.mapstruct.Mapper;

@Mapper
public interface PartyMapper {

    Party toParty(ca.bc.gov.open.jag.efilingapi.api.model.Party party);

}
