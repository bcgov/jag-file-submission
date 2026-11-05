package ca.bc.gov.open.jag.efilingapi.filingpackage.mapper;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackagePayment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewRushOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface FilingPackageMapper {

    @Mapping(target = "court.fileNumber", source = "court.fileNumber")
    @Mapping(target = "court.courtClass", source = "court.courtClass")
    @Mapping(target = "court.level", source = "court.level")
    @Mapping(target = "court.levelDescription", source = "court.levelDescription")
    @Mapping(target = "court.division", source = "court.division")
    @Mapping(target = "court.participatingClass", source = "court.participatingClass")
    @Mapping(target = "court.location", source = "court.locationName")
    @Mapping(target = "court.agencyId", source = "court.locationId")
    @Mapping(target = "court.locationDescription", source = "court.locationDescription")
    @Mapping(target = "court.classDescription", source = "court.classDescription")
    @Mapping(target = "submittedBy.firstName",  source = "firstName")
    @Mapping(target = "submittedBy.lastName",  source = "lastName")
    @Mapping(target = "submittedDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(filingPackage.getSubmittedDate()))")
    @Mapping(target = "packageNumber", source = "packageNo")
    @Mapping(target = "filingComments", source = "filingCommentsTxt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "links.packageHistoryUrl", source = "packageLinks.packageHistoryUrl")
    @Mapping(target = "organizationParties", source = "organizations")
    @Mapping(target = "rush", source = "rushOrder")
    FilingPackage toResponseFilingPackage(ReviewFilingPackage filingPackage);

    List<Document> toDocuments(List<ReviewDocument> file);

    @Mapping(target = "identifier", source = "documentId")
    @Mapping(target = "documentId", source = "parentDocumentId")
    @Mapping(target = "documentProperties.name", source = "fileName")
    @Mapping(target = "documentProperties.type", source = "documentTypeCd")
    @Mapping(target = "description", source = "documentType")
    @Mapping(target = "filingDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(file.getDateFiled()))")
    @Mapping(target = "status.description", source = "status")
    @Mapping(target = "status.code", source = "statusCode")
    @Mapping(target = "status.changeDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(reviewDocument.getStatusDate()))")
    @Mapping(target = "paymentProcessed", source = "paymentProcessed")
    @Mapping(target = "rushRequired", source = "rushRequired")
    Document toDocument(ReviewDocument file);

    List<Individual> toParties(List<ca.bc.gov.open.jag.efilingcommons.model.Individual> parties);

    @Mapping(target = "partyDescription", source = "partyTypeDesc")
    @Mapping(target = "roleType", source = "roleTypeCd")
    @Mapping(target = "roleDescription", source = "roleTypeDesc")
    Individual toParty(ca.bc.gov.open.jag.efilingcommons.model.Individual individual);

    List<Organization> toOrganizationParties(List<ca.bc.gov.open.jag.efilingcommons.model.Organization> organizations);

    @Mapping(target = "partyDescription", source = "partyTypeDesc")
    @Mapping(target = "roleType", source = "roleTypeCd")
    @Mapping(target = "roleDescription", source = "roleTypeDesc")
    Organization toOrganization(ca.bc.gov.open.jag.efilingcommons.model.Organization organization);

    List<Payment> toPayments(List<PackagePayment> payments);

    @Mapping(target = "feeExempt", source = "feeExmpt")
    @Mapping(target = "paymentCategory", source = "paymentCategory")
    @Mapping(target = "paymentDescription", source = "transactionDesc")
    @Mapping(target = "processedAmount", source = "processedAmt")
    @Mapping(target = "submittedAmount", source = "submittedAmt")
    @Mapping(target = "serviceIdentifier", source = "serviceId")
    @Mapping(target = "transactionDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(payment.getTransactionDtm()))")
    Payment toPayment(PackagePayment payment);

    @Mapping(target = "reason", source = "rushFilingReasonTxt")
    @Mapping(target = "rushType", expression = "java(ca.bc.gov.open.jag.efilingapi.filingpackage.util.RushMappingUtils.getRushType(reviewRushOrder.getProcessReasonCd()))")
    @Mapping(target = "status", source = "currentStatusDsc")
    @Mapping(target = "email", source = "contactEmailTxt")
    @Mapping(target = "firstName", source = "contactFirstGivenNm")
    @Mapping(target = "organization", source = "contactOrganizationNm")
    @Mapping(target = "phoneNumber", source = "contactPhoneNo")
    @Mapping(target = "lastName", source = "contactSurnameNm")
    @Mapping(target = "countryCode", source = "ctryId")
    @Mapping(target = "country", source = "countryDsc")
    @Mapping(target = "statusReason", source = "processingCommentTxt")
    @Mapping(target = "courtDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(reviewRushOrder.getCourtOrderDt()))")
    @Mapping(target = "supportingDocuments", source = "supportDocs")
    Rush toRush(ReviewRushOrder reviewRushOrder);

    @Mapping(target = "fileName", source = "clientFileNm")
    @Mapping(target = "identifier", source = "objectGuid")
    RushDocument toRushDocument(ca.bc.gov.open.jag.efilingcommons.submission.models.review.RushDocument rushDocument);

}
