package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.Rush;
import ca.bc.gov.open.jag.efilingapi.api.model.RushDocument;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.model.RushProcessing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RushProcessingMapper {
    RushProcessing toRushProcessing(Rush rush);

    @Mapping(target = "name", source="fileName")
    Document toDocument(RushDocument rushDocument);

}
