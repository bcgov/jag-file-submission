package ca.bc.gov.open.jag.efilingapi.courts.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CourtLocationMapper {

    List<CourtLocation> toCourtLocationList(List<InternalCourtLocation> courtLocations);
}
