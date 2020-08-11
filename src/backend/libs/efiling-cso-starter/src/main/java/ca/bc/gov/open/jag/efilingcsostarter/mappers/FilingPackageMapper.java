package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper
public interface FilingPackageMapper {
    @Mapping(target = "applicationCd", source = "efilingFilingPackage.applicationCd")
    @Mapping(target = "applicationReferenceGuid", source = "efilingFilingPackage.applicationReferenceGuid")
    @Mapping(target = "autoProcessEndDtm", source = "efilingFilingPackage.autoProcessEndDtm")
    @Mapping(target = "autoProcessStartDtm", source = "efilingFilingPackage.autoProcessStartDtm")
    @Mapping(target = "automatedProcessYn", source = "efilingFilingPackage.automatedProcessYn")
    @Mapping(target = "cfcsaYn", source = "efilingFilingPackage.cfcsaYn")
    @Mapping(target = "checkedOutByAgenId", source = "efilingFilingPackage.checkedOutByAgenId")
    @Mapping(target = "checkedOutByPaasSeqNo", source = "efilingFilingPackage.checkedOutByPaasSeqNo")
    @Mapping(target = "checkedOutByPartId", source = "efilingFilingPackage.checkedOutByPartId")
    @Mapping(target = "checkedOutDtm", source = "efilingFilingPackage.checkedOutDtm")
    @Mapping(target = "clientFileNo", source = "efilingFilingPackage.clientFileNo")
    @Mapping(target = "courtFileNo", source = "efilingFilingPackage.courtFileNo")
    @Mapping(target = "delayProcessing", source = "efilingFilingPackage.delayProcessing")
    @Mapping(target = "documents", source = "efilingFilingPackage.documents")
    @Mapping(target = "entDtm", source = "efilingFilingPackage.entDtm")
    @Mapping(target = "entUserId", source = "efilingFilingPackage.entUserId")
    @Mapping(target = "existingCourtFileYn", source = "efilingFilingPackage.existingCourtFileYn")
    @Mapping(target = "feeExemptYn", source = "efilingFilingPackage.feeExemptYn")
    @Mapping(target = "ldcxCourtClassCd", source = "efilingFilingPackage.ldcxCourtClassCd")
    @Mapping(target = "ldcxCourtDivisionCd", source = "efilingFilingPackage.ldcxCourtDivisionCd")
    @Mapping(target = "ldcxCourtLevelCd", source = "efilingFilingPackage.ldcxCourtLevelCd")
    @Mapping(target = "notificationEmailTxt", source = "efilingFilingPackage.notificationEmailTxt")
    @Mapping(target = "notificationRequiredYn", source = "efilingFilingPackage.notificationRequiredYn")
    @Mapping(target = "packageControls", source = "efilingFilingPackage.packageControls")
    @Mapping(target = "packageId", source = "efilingFilingPackage.packageId")
    @Mapping(target = "processingCompleteYn", source = "efilingFilingPackage.processingCompleteYn")
    @Mapping(target = "resubmissionOfPackageId", source = "efilingFilingPackage.resubmissionOfPackageId")
    @Mapping(target = "reviewerNotesTxt", source = "efilingFilingPackage.reviewerNotesTxt")
    @Mapping(target = "submitDtm", source = "efilingFilingPackage.submitDtm")
    @Mapping(target = "submittedByAccountId", source = "efilingFilingPackage.submittedByAccountId")
    @Mapping(target = "submittedByClientId", source = "efilingFilingPackage.submittedByClientId")
    @Mapping(target = "submittedToAgenId", source = "efilingFilingPackage.submittedToAgenId")
    @Mapping(target = "submitterCommentTxt", source = "efilingFilingPackage.submitterCommentTxt")
    @Mapping(target = "serviceId", source = "efilingFilingPackage.serviceId")
    FilingPackage toFilingPackage(EfilingFilingPackage efilingFilingPackage, BigDecimal serviceId);
}
