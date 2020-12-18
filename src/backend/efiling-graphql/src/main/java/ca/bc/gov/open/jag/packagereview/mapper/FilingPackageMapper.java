package ca.bc.gov.open.jag.packagereview.mapper;

import ca.bc.gov.open.jag.packagereview.model.FilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FilingPackageMapper {

    @Mapping(target = "court.fileNumber", source = "filingPackage.court.fileNumber")
    @Mapping(target = "court.classification", source = "filingPackage.court.courtClass")
    @Mapping(target = "court.level", source = "filingPackage.court.level")
    @Mapping(target = "court.levelDescription", source = "filingPackage.court.levelDescription")
    @Mapping(target = "court.locationCode", source = "filingPackage.court.location")
    @Mapping(target = "court.locationName", source = "filingPackage.court.locationDescription")
    FilingPackage toResponseFilingPackage(ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage filingPackage);
}
