package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.Document;
import ca.bc.gov.ag.csows.filing.CsoParty;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.PackageAuthority;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FilingPackageMapper {

    @Mapping(target = "existingCourtFileYn", expression = "java(org.apache.commons.lang3.StringUtils.isNotBlank(filingPackage.getCourt().getFileNumber()))")
    @Mapping(target = "processingCompleteYn", constant = "false")
    @Mapping(target = "feeExemptYn", expression = "java(!(filingPackage.getSubmissionFeeAmount() != null && filingPackage.getSubmissionFeeAmount().compareTo(BigDecimal.ZERO) > 0))")
    @Mapping(target = "cfcsaYn", constant = "false")
    @Mapping(target = "notificationRequiredYn", constant = "true")
    @Mapping(target = "resubmissionOfPackageId", source = "filingPackage.packageNumber")

    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")

    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "ldcxCourtClassCd", source = "filingPackage.court.courtClass")
    @Mapping(target = "ldcxCourtLevelCd", source = "filingPackage.court.level")
    @Mapping(target = "submittedToAgenId", source = "filingPackage.court.agencyId")
    @Mapping(target = "ldcxCourtDivisionCd", source = "filingPackage.court.division")
    @Mapping(target = "applicationCd", source = "filingPackage.applicationCode")
    @Mapping(target = "automatedProcessYn", source = "filingPackage.autoProcessing")

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "submittedByAccountId", source = "accountDetails.accountId")
    @Mapping(target = "submittedByClientId", source = "accountDetails.clientId")

    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "submitDtm", source = "submittedDate")
    @Mapping(target = "documents", source = "documents")
    @Mapping(target = "parties", source = "csoParties")
    @Mapping(target = "packageControls", source = "packageControls")
    FilingPackage toFilingPackage(
            ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage filingPackage,
            AccountDetails accountDetails,
            BigDecimal serviceId,
            XMLGregorianCalendar submittedDate,
            List<Document> documents,
            List<CsoParty> csoParties,
            List<PackageAuthority> packageControls);
}
