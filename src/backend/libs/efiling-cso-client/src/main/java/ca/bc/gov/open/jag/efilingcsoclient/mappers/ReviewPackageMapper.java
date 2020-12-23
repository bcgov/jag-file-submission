package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.status.FilePackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewPackageMapper {

    @Mapping(target = "reviewCourt.courtClass", source = "courtClassCd")
    @Mapping(target = "reviewCourt.fileNumber", source = "courtFileNo")
    @Mapping(target = "reviewCourt.level", source = "courtLevelCd")
    @Mapping(target = "reviewCourt.location", source = "courtLocationCd")
    @Mapping(target = "reviewCourt.locationDescription", source = "courtLocationName")
    ReviewPackage toReviewPackage(FilePackage filePackage);
}


