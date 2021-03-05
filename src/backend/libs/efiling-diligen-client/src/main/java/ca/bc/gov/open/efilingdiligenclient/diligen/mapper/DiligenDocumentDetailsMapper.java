package ca.bc.gov.open.efilingdiligenclient.diligen.mapper;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003DataFileDetails;
import org.mapstruct.Mapper;


@Mapper
public interface DiligenDocumentDetailsMapper {
    DiligenDocumentDetails toDiligenDocumentDetails(InlineResponse2003DataFileDetails fileDetails);
}
