package ca.bc.gov.open.jag.efilingapi.submission.mappers;


import ca.bc.gov.open.jag.efilingapi.api.model.Document;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface EfilingFilingPackageMapper {
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
    @Mapping(target = "parties", source = "parties")
    EfilingFilingPackage toEfilingFilingPackage(Submission submission, List<EfilingParties> parties);

    @Mapping(target = "amendsAnotherDocumentYn", source = "document.isAmendment", defaultValue = "false")
    @Mapping(target = "clientFileNameTxt", source = "document.name")
    @Mapping(target = "documentDescriptionTxt", source = "document.description")
    @Mapping(target = "documentSubtypeCd", constant = "document.subType" )
    @Mapping(target = "documentTypeCd", source = "document.type")
    @Mapping(target = "entUserId", source = "submission.accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    //TODO this is the constructed file name
    @Mapping(target = "filePath", source = "document.name")
    @Mapping(target = "uploadStateCd", constant = SubmissionConstants.SUBMISSION_UPLOAD_STATE_CD)
    @Mapping(target = "milestones", source = "milestones")
    @Mapping(target = "payments", source = "payments")
    @Mapping(target = "statuses", source = "statuses")
    EfilingDocument toEfilingDocument(Document document, Submission submission, List<EfilingDocumentMilestone> milestones,
                                      List<EfilingDocumentPayment> payments, List<EfilingDocumentStatus> statuses);

    @Mapping(target = "entUserId", source = "submission.accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneTypeCd",  constant = "ASUB")
    @Mapping(target = "milestoneSeqNo",  constant = "1")
    EfilingDocumentMilestone toEfilingDocumentMilestone(Document document, Submission submission);

    @Mapping(target = "entUserId", source = "submission.accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "paymentStatusCd",  constant = "WAT")
    @Mapping(target = "statutoryFeeAmt",  source = "document.statutoryFeeAmount")
    @Mapping(target = "paymentSeqNo",  constant = "1")
    EfilingDocumentPayment toEfilingDocumentPayment(Document document, Submission submission);

    @Mapping(target = "entUserId", source = "submission.accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "statusDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "documentStatusTypeCd",  constant = SubmissionConstants.SUBMISSION_DOCUMENT_STATUS_TYPE_CD)
    @Mapping(target = "documentStatusSeqNo",  constant = "1")
    EfilingDocumentStatus toEfilingDocumentStatus(Document document, Submission submission);

    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "privilegeCd", constant = SubmissionConstants.SUBMISSION_PRIVILEGE_CD)
    EfilingPackageAuthority toPackageAuthority(Submission submission);

    @Mapping(target = "partyTypeCd", constant = SubmissionConstants.SUBMISSION_PARTY_TYPE_CD)
    @Mapping(target = "roleTypeCd", constant = SubmissionConstants.SUBMISSION_ROLE_TYPE_CD)
    @Mapping(target = "current.entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "current.entUserId", source = "accountDetails.clientId")
    @Mapping(target = "current.firstGivenNm", source = "accountDetails.firstName")
    @Mapping(target = "current.identificationDetailSeqNo", constant = "1")
    @Mapping(target = "current.nameTypeCd", constant = SubmissionConstants.SUBMISSION_NAME_TYPE_CD)
    @Mapping(target = "current.secondGivenNm", source = "accountDetails.middleName")
    @Mapping(target = "current.surnameNm", source = "accountDetails.lastName")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    EfilingParties toEfilingParties(Submission submission);

    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "internalClientNumber", source = "accountDetails.internalClientNumber")
    @Mapping(target = "courtFileNumber", source = "filingPackage.court.fileNumber")
    @Mapping(target = "serviceTypeCd", constant = SubmissionConstants.SUBMISSION_FEE_TYPE)
    @Mapping(target = "serviceSubtypeCd", constant = SubmissionConstants.SUBMISSION_FEE_SUB_TYPE)
    @Mapping(target = "entryUserId", source = "accountDetails.clientId")
    @Mapping(target = "submissionFeeAmount", source = "filingPackage.submissionFeeAmount")
    EfilingService toEfilingService(Submission submission);

}
