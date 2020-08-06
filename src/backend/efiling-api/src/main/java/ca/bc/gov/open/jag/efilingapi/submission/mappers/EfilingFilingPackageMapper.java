package ca.bc.gov.open.jag.efilingapi.submission.mappers;


import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EfilingFilingPackageMapper {
    @Mapping(target = "applicationCd", source = "clientApplication.type")
    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "clientId")
    @Mapping(target = "existingCourtFileYn", defaultValue = "false")
    @Mapping(target = "feeExemptYn", defaultValue = "false")
    @Mapping(target = "ldcxCourtClassCd", source = "filingPackage.court.courtClass")
    @Mapping(target = "ldcxCourtLevelCd", source = "filingPackage.court.level")
    EfilingFilingPackage toEfilingFilingPackage(Submission submission);
}
