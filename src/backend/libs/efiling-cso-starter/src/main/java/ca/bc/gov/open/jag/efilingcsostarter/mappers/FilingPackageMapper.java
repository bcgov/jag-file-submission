package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.filing.CivilDocument;
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

    @Mapping(target = "existingCourtFileYn", constant = "false")
    @Mapping(target = "processingCompleteYn", constant = "false")
    @Mapping(target = "feeExemptYn", constant = "false")
    @Mapping(target = "cfcsaYn", constant = "false")
    @Mapping(target = "automatedProcessYn", constant = "false")
    @Mapping(target = "notificationRequiredYn", constant = "true")

    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")

    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "ldcxCourtClassCd", source = "filingPackage.court.courtClass")
    @Mapping(target = "ldcxCourtLevelCd", source = "filingPackage.court.level")
    @Mapping(target = "submittedToAgenId", source = "filingPackage.court.agencyId")
    @Mapping(target = "ldcxCourtDivisionCd", source = "filingPackage.court.division")
    @Mapping(target = "applicationCd", source = "filingPackage.applicationType")

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "submittedByAccountId", source = "accountDetails.accountId")
    @Mapping(target = "submittedByClientId", source = "accountDetails.clientId")

    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "submitDtm", source = "submittedDate")
    @Mapping(target = "documents", source = "documents")
    @Mapping(target = "parties", source = "csoParties")
    @Mapping(target = "packageControls", source = "packageControls")
    FilingPackage toFilingPackage(
            ca.bc.gov.open.jag.efilingcommons.model.FilingPackage filingPackage,
            AccountDetails accountDetails,
            BigDecimal serviceId,
            XMLGregorianCalendar submittedDate,
            List<CivilDocument> documents,
            List<CsoParty> csoParties,
            List<PackageAuthority> packageControls);
}
