package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.Rush;
import ca.bc.gov.open.jag.efilingapi.api.model.RushDocument;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.model.RushProcessing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RushProcessingMapper {

    @Mapping(target="rushType", source="rushType", defaultValue = "")
    @Mapping(target="phoneNumber", source="phoneNumber", defaultValue = "")
    @Mapping(target="reason", source="reason", defaultValue = "")
    @Mapping(target="organization", source="organization", defaultValue = "")
    @Mapping(target="country", source="country", defaultValue = "")
    @Mapping(target="countryCode", source="countryCode", defaultValue = "")
    RushProcessing toRushProcessing(Rush rush);

    @Mapping(target = "name", source = "fileName")
    @Mapping(target = "serverFileName", source = "fileName")
    Document toDocument(RushDocument rushDocument);

}
