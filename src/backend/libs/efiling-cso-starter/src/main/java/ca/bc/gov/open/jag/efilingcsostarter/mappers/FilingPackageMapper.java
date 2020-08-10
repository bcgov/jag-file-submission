package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FilingPackageMapper {
    @Mapping(target = "applicationCd", source = "applicationCd")
    @Mapping(target = "applicationReferenceGuid", source = "applicationReferenceGuid")
    @Mapping(target = "autoProcessEndDtm", source = "autoProcessEndDtm")
    @Mapping(target = "autoProcessStartDtm", source = "autoProcessStartDtm")
    @Mapping(target = "automatedProcessYn", source = "automatedProcessYn")
    @Mapping(target = "cfcsaYn", source = "cfcsaYn")
    @Mapping(target = "checkedOutByAgenId", source = "checkedOutByAgenId")
    @Mapping(target = "checkedOutByPaasSeqNo", source = "checkedOutByPaasSeqNo")
    @Mapping(target = "checkedOutByPartId", source = "checkedOutByPartId")
    @Mapping(target = "checkedOutDtm", source = "checkedOutDtm")
    @Mapping(target = "clientFileNo", source = "clientFileNo")
    @Mapping(target = "courtFileNo", source = "courtFileNo")
    @Mapping(target = "delayProcessing", source = "delayProcessing")
    @Mapping(target = "documents", source = "documents")
    @Mapping(target = "entDtm", source = "entDtm")
    @Mapping(target = "entUserId", source = "entUserId")
    @Mapping(target = "existingCourtFileYn", source = "existingCourtFileYn")
    @Mapping(target = "feeExemptYn", source = "feeExemptYn")
    @Mapping(target = "ldcxCourtClassCd", source = "ldcxCourtClassCd")
    @Mapping(target = "ldcxCourtDivisionCd", source = "ldcxCourtDivisionCd")
    @Mapping(target = "ldcxCourtLevelCd", source = "ldcxCourtLevelCd")
    @Mapping(target = "notificationEmailTxt", source = "notificationEmailTxt")
    @Mapping(target = "notificationRequiredYn", source = "notificationRequiredYn")
    @Mapping(target = "packageControls", source = "packageControls")
    @Mapping(target = "packageId", source = "packageId")
    @Mapping(target = "processingCompleteYn", source = "processingCompleteYn")
    @Mapping(target = "resubmissionOfPackageId", source = "resubmissionOfPackageId")
    @Mapping(target = "reviewerNotesTxt", source = "reviewerNotesTxt")
    @Mapping(target = "submitDtm", source = "submitDtm")
    @Mapping(target = "submittedByAccountId", source = "submittedByAccountId")
    @Mapping(target = "submittedByClientId", source = "submittedByClientId")
    @Mapping(target = "submittedToAgenId", source = "submittedToAgenId")
    @Mapping(target = "submitterCommentTxt", source = "submitterCommentTxt")
    FilingPackage toFilingPackage(EfilingFilingPackage efilingFilingPackage);
}
