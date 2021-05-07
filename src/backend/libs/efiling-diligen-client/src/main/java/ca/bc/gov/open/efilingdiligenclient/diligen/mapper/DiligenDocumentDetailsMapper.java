package ca.bc.gov.open.efilingdiligenclient.diligen.mapper;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import org.openapitools.client.model.Field;
import org.openapitools.client.model.InlineResponse2003DataFileDetails;
import org.openapitools.client.model.ProjectFieldsResponse;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface DiligenDocumentDetailsMapper {
    DiligenDocumentDetails toDiligenDocumentDetails(InlineResponse2003DataFileDetails fileDetails, List<Field> answers, ProjectFieldsResponse projectFieldsResponse);

}
