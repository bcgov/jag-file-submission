package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.open.jag.efilingcommons.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Mapper
public interface DocumentMapper {


    String SUBMISSION_UPLOAD_STATE_CD = "CMPL";
    String SUBMISSION_DOCUMENT_STATUS_TYPE_CD = "SUB";
    String PAYMENT_STATUS_CD = "NREQ";
    String XML_DOCUMENT_INSTANCE_YN = "false";
    String DOCUMENT_SUB_TYPE_CD = "ODOC";


    @Mapping(target = "amendsAnotherDocumentYn", source = "document.isAmendment", defaultValue = "false")
    @Mapping(target = "clientFileNameTxt", source = "document.name")
    @Mapping(target = "documentSubtypeCd", constant = DOCUMENT_SUB_TYPE_CD)
    @Mapping(target = "documentTypeCd", source = "document.type")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    //TODO this is the constructed file name
    @Mapping(target = "filePath", source = "filePath")
    @Mapping(target = "fileServer", source = "serverHost")

    @Mapping(target = "uploadStateCd", constant = SUBMISSION_UPLOAD_STATE_CD)

    @Mapping(target = "jsonObject", expression = "java(java.lang.String.valueOf(document.getData()))")
    @Mapping(target = "xmlDocumentInstanceYn", constant = XML_DOCUMENT_INSTANCE_YN)

    @Mapping(target = "milestones", source = "milestones")
    @Mapping(target = "payments", source = "payments")
    @Mapping(target = "statuses", source = "statuses")

    EfilingDocument toEfilingDocument(
            Integer index,
            Document document,
            AccountDetails accountDetails,
            FilingPackage filingPackage,
            String filePath,
            String serverHost,
            List<EfilingDocumentMilestone> milestones,
            List<EfilingDocumentPayment> payments,
            List<EfilingDocumentStatus> statuses);

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneTypeCd",  constant = "ASUB")
    @Mapping(target = "milestoneSeqNo",  constant = "1")
    EfilingDocumentMilestone toActualSubmittedDate(AccountDetails accountDetails);

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "milestoneDtm",  source = "xmlGregorianCalendar")
    @Mapping(target = "milestoneTypeCd",  constant = "CSUB")
    @Mapping(target = "milestoneSeqNo",  constant = "2")
    EfilingDocumentMilestone toComputedSubmittedDate(AccountDetails accountDetails, XMLGregorianCalendar xmlGregorianCalendar);

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "paymentStatusCd",  constant = PAYMENT_STATUS_CD)
    @Mapping(target = "statutoryFeeAmt",  source = "document.statutoryFeeAmount")
    @Mapping(target = "paymentSeqNo",  constant = "1")
    EfilingDocumentPayment toEfilingDocumentPayment(Document document, AccountDetails accountDetails);

    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "statusDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "documentStatusTypeCd",  constant = SUBMISSION_DOCUMENT_STATUS_TYPE_CD)
    @Mapping(target = "documentStatusSeqNo",  constant = "1")
    EfilingDocumentStatus toEfilingDocumentStatus(Document document, AccountDetails accountDetails);

}
