package ca.bc.gov.open.jag.efilingapi.submission.mappers;


import ca.bc.gov.open.jag.efilingapi.api.model.Document;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingDocument;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingPackageAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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

    @Mapping(target = "amendsAnotherDocumentYn", source = "isAmendment")
    @Mapping(target = "clientFileNameTxt", source = "name")
    @Mapping(target = "documentDescriptionTxt", source = "description")
    @Mapping(target = "documentTypeCd", source = "type")
    EfilingDocument toEfilingDocument(Document documentList);

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "entUserId", source = "clientId")
    EfilingPackageAuthority toPackageAuthority(Submission submission);
}
