package ca.bc.gov.open.jag.efilingapi.submission.mappers;


import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EfilingFilingPackageMapper {

    @Mapping(target = "applicationCd", source = "submission.clientApplication.type")
    @Mapping(target = "courtFileNo", source = "submission.filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "submission.accountDetails.clientId")
    @Mapping(target = "existingCourtFileYn", constant = "false")
    @Mapping(target = "processingCompleteYn", constant = "false")
    @Mapping(target = "feeExemptYn", constant = "false")
    @Mapping(target = "cfcsaYn", constant = "false")
    @Mapping(target = "automatedProcessYn", constant = "false")
    @Mapping(target = "notificationRequiredYn", constant = "false")
    @Mapping(target = "ldcxCourtClassCd", source = "submission.filingPackage.court.courtClass")
    @Mapping(target = "ldcxCourtLevelCd", source = "submission.filingPackage.court.level")
    @Mapping(target = "submittedToAgenId", source = "submission.filingPackage.court.agencyId")
    @Mapping(target = "submittedByAccountId", source = "submission.accountDetails.accountId")
    @Mapping(target = "submittedByClientId", source = "submission.accountDetails.clientId")
    @Mapping(target = "ldcxCourtDivisionCd", source = "submission.filingPackage.court.division")
    EfilingFilingPackage toEfilingFilingPackage(Submission submission);

}
