package ca.bc.gov.open.jag.efilingapi.filingpackage.mapper;

import ca.bc.gov.open.jag.efilingapi.api.model.Document;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface FilingPackageMapper {

    @Mapping(target = "court.fileNumber", source = "court.fileNumber")
    @Mapping(target = "court.courtClass", source = "court.courtClass")
    @Mapping(target = "court.level", source = "court.level")
    @Mapping(target = "court.levelDescription", source = "court.levelDescription")
    @Mapping(target = "court.location", source = "court.locationName")
    FilingPackage toResponseFilingPackage(ReviewFilingPackage filingPackage);

    List<Document> toDocuments(List<ReviewDocument> file);

    //TODO: extend document to add additional fields
    @Mapping(target = "name", source = "fileName")
    @Mapping(target = "type", source = "documentTypeCd")
    Document toDocument(ReviewDocument file);


}
