package ca.bc.gov.open.efilingdiligenclient.diligen.mapper;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003DataFileDetails;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface DiligenDocumentDetailsMapper {
    DiligenDocumentDetails toDiligenDocumentDetails(InlineResponse2003DataFileDetails fileDetails, List<Field> answers);

}
