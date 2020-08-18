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

    @Mapping(target = "amendsAnotherDocumentYn", source = "document.isAmendment", defaultValue = "false")
    @Mapping(target = "clientFileNameTxt", source = "document.name")
    @Mapping(target = "documentDescriptionTxt", source = "document.description")
    @Mapping(target = "documentSubtypeCd", constant = "ODOC" )
    @Mapping(target = "documentTypeCd", source = "document.type")
    @Mapping(target = "entUserId", source = "submission.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    //TODO this is the constructed file name
    @Mapping(target = "filePath", source = "document.name")
    //TODO this will be pulled from the config
    @Mapping(target = "fileServer", source = "serverName")
    @Mapping(target = "uploadStateCd", constant = SubmissionConstants.SUBMISSION_UPLOAD_STATE_CD)
    @Mapping(target = "milestones", source = "milestones")
    @Mapping(target = "payments", source = "payments")
    @Mapping(target = "statuses", source = "statuses")
    @Mapping(target = "packageSeqNo", source = "index")
    EfilingDocument toEfilingDocument(Document document, Submission submission, Integer index,
                                      String serverName, List<EfilingDocumentMilestone> milestones,
                                      List<EfilingDocumentPayment> payments, List<EfilingDocumentStatus> statuses);

    @Mapping(target = "entUserId", source = "submission.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneTypeCd",  constant = "ASUB")
    @Mapping(target = "milestoneSeqNo",  constant = "1")
    EfilingDocumentMilestone toEfilingDocumentMilestone(Document document, Submission submission);

    @Mapping(target = "entUserId", source = "submission.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "paymentStatusCd",  constant = "WAT")
    @Mapping(target = "statutoryFeeAmt",  source = "document.statutoryFeeAmount")
    @Mapping(target = "paymentSeqNo",  constant = "1")
    EfilingDocumentPayment toEfilingDocumentPayment(Document document, Submission submission);

    @Mapping(target = "entUserId", source = "submission.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "statusDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "documentStatusTypeCd",  constant = SubmissionConstants.SUBMISSION_DOCUMENT_STATUS_TYPE_CD)
    @Mapping(target = "documentStatusSeqNo",  constant = "1")
    EfilingDocumentStatus toEfilingDocumentStatus(Document document, Submission submission);

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "entUserId", source = "clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "privilegeCd", constant = SubmissionConstants.SUBMISSION_PRIVILEGE_CD)
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
