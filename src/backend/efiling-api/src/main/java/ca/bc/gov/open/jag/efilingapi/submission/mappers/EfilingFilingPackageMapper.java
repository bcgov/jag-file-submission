package ca.bc.gov.open.jag.efilingapi.submission.mappers;


import ca.bc.gov.open.jag.efilingapi.api.model.Document;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingDocument;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPackageAuthority;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EfilingFilingPackageMapper {
    @Mapping(target = "applicationCd", source = "clientApplication.type")
    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "clientId")
    @Mapping(target = "existingCourtFileYn", constant = "false")
    @Mapping(target = "processingCompleteYn", constant = "false")
    @Mapping(target = "feeExemptYn", constant = "false")
    @Mapping(target = "cfcsaYn", constant = "false")
    @Mapping(target = "automatedProcessYn", constant = "false")
    @Mapping(target = "notificationRequiredYn", constant = "false")
    @Mapping(target = "ldcxCourtClassCd", source = "filingPackage.court.courtClass")
    @Mapping(target = "ldcxCourtLevelCd", source = "filingPackage.court.level")
    @Mapping(target = "submittedToAgenId", source = "filingPackage.court.agencyId")
    @Mapping(target = "submittedByAccountId", source = "accountId")
    @Mapping(target = "submittedByClientId", source = "clientId")
    @Mapping(target = "ldcxCourtDivisionCd", source = "filingPackage.court.division")
    EfilingFilingPackage toEfilingFilingPackage(Submission submission);

    @Mapping(target = "amendsAnotherDocumentYn", source = "isAmendment")
    @Mapping(target = "clientFileNameTxt", source = "name")
    @Mapping(target = "documentDescriptionTxt", source = "description")
    @Mapping(target = "documentTypeCd", source = "type")
    EfilingDocument toEfilingDocument(Document document);

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "entUserId", source = "clientId")
    EfilingPackageAuthority toPackageAuthority(Submission submission);

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "internalClientNumber", source = "internalClientNumber")
    @Mapping(target = "courtFileNumber", source = "filingPackage.court.fileNumber")
    @Mapping(target = "serviceTypeCd", constant = SubmissionConstants.SUBMISSION_FEE_TYPE)
    @Mapping(target = "serviceSubtypeCd", constant = SubmissionConstants.SUBMISSION_FEE_SUB_TYPE)
    @Mapping(target = "entryUserId", source = "clientId")
    @Mapping(target = "submissionFeeAmount", source = "filingPackage.submissionFeeAmount")
    EfilingService toEfilingService(Submission submission);

}
