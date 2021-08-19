package ca.bc.gov.open.jag.efilingapi.filingpackage.mapper;

import ca.bc.gov.open.jag.efilingapi.api.model.ActionDocument;
import ca.bc.gov.open.jag.efilingapi.api.model.ActionRequiredDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ActionRequiredDetailsMapper {

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "documents", source = "actionDocuments")
    @Mapping(target = "packageIdentifier", source = "filingPackage.packageNo")
    ActionRequiredDetails toActionRequiredDetails(ReviewFilingPackage filingPackage, BigDecimal clientId, List<ActionDocument> actionDocuments);

    @Mapping(target = "id", source = "documentId")
    @Mapping(target = "type", source = "documentTypeCd")
    @Mapping(target = "status", source = "statusCode")
    ActionDocument toActionDocument(ReviewDocument file);

}
