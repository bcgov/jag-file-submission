package ca.bc.gov.open.jag.efilingapi.filingpackage.mapper;

import ca.bc.gov.open.jag.efilingapi.api.model.Document;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Party;
import ca.bc.gov.open.jag.efilingapi.api.model.Payment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackagePayment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
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
    FilingPackage toResponseFilingPackage(ReviewFilingPackage filingPackage);

    List<Document> toDocuments(List<ReviewDocument> file);

    @Mapping(target = "identifier", source = "documentId")
    @Mapping(target = "documentProperties.name", source = "fileName")
    @Mapping(target = "documentProperties.type", source = "documentTypeCd")
    @Mapping(target = "description", source = "documentType")
    @Mapping(target = "filingDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(file.getDateFiled()))")
    @Mapping(target = "status.description", source = "status")
    @Mapping(target = "status.code", source = "statusCode")
    @Mapping(target = "status.changeDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(reviewDocument.getStatusDate()))")
    @Mapping(target = "paymentProcessed", source = "paymentProcessed")
    Document toDocument(ReviewDocument file);

    List<Party> toParties(List<ca.bc.gov.open.jag.efilingcommons.model.Party> parties);

    @Mapping(target = "partyType", source = "partyTypeCd")
    @Mapping(target = "partyDescription", source = "partyTypeDesc")
    @Mapping(target = "roleType", source = "roleTypeCd")
    @Mapping(target = "roleDescription", source = "roleTypeDesc")
    Party toParty(ca.bc.gov.open.jag.efilingcommons.model.Party party);


    List<Payment> toPayments(List<PackagePayment> payments);

    @Mapping(target = "feeExempt", source = "feeExmpt")
    @Mapping(target = "paymentCategory", source = "paymentCategory")
    @Mapping(target = "processedAmount", source = "processedAmt")
    @Mapping(target = "submittedAmount", source = "submittedAmt")
    @Mapping(target = "serviceIdentifier", source = "serviceId")
    @Mapping(target = "transactionDate", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.toIsoDate(payment.getTransactionDtm()))")
    Payment toPayment(PackagePayment payment);

}
