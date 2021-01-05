package ca.bc.gov.open.jag.packagereview.mapper;

import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.packagereview.model.FilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface FilingPackageMapper {

    @Mapping(target = "court.fileNumber", source = "court.fileNumber")
    @Mapping(target = "court.classification", source = "court.courtClass")
    @Mapping(target = "court.level", source = "court.level")
    @Mapping(target = "court.levelDescription", source = "court.levelDescription")
    @Mapping(target = "court.locationName", source = "court.locationName")
    FilingPackage toResponseFilingPackage(ReviewFilingPackage filingPackage);

}
