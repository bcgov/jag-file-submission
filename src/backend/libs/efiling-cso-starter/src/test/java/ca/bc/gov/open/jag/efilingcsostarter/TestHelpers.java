package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import org.joda.time.DateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class TestHelpers {
    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    public static final String CLIENT_REFERENCE_TXT = "CLIENTREFERENCETXT";
    public static final String COURT_FILE_NUMBER = "COURTFILENUMBER";
    public static final String DOCUMENTS_PROCESSED = "DOCUMENTPROCESSED";
    public static final Date DATE = DateTime.parse("2018-05-05T11:50:55").toDate();
    public static final String SERVICE_RECEIVED_DTM_TEXT = "SERVICERECIEVEDTXT";
    public static final String SERVICE_SUBTYPE_CD = "SUBTYPE";
    public static final String SERVICE_TYPE_CD = "TYPE_CD";
    public static final String SERVICE_TYPE_DESC = "TYPE_DESC";
    private static final String APPROVAL_CD = "APPROVALCD";
    private static final String BC_ONLINE_ACCOUNT_NO = "bcOnlineAccountNo";
    private static final String BC_ONLINE_FEE_CODE_TXT = "bcOnlineFeeCodeTxt";
    private static final String BC_ONLINE_RESPONSE_TYPE_TXT = "bcOnlineResponseTypeTxt";
    private static final String BC_ONLINE_RETURN_CD = "bcOnlineReturnCd";
    private static final String BC_ONLINE_RETURN_MESSAGE_TXT = "bcOnlineReturnMessageTxt";
    private static final String BC_ONLINE_SEQUENCE_TXT = "bcOnlineSequenceTxt";
    private static final String BC_ONLINE_TRANSACTION_TYPE_CD = "bcOnlineTransactionTypeCd";
    private static final String CREDIT_CARD_TYPE_CD = "creditCardTypeCd";
    private static final String ENT_USER_ID = "entUserId";
    private static final String INTERNAL_CLIENT_NO = "internalClientNo";
    private static final String INVOICE_NO = "invoiceNo";
    private static final String REFERENCE_MESSAGE_TXT = "referenceMessageTxt";
    private static final String RESPONSE_CD = "responseCd";
    private static final String SESSION_KEY_NO = "sessionKeyNo";
    private static final String TERMINAL_IDENTIFIER_NO = "terminalIdentifierNo";
    private static final String TRANSACTION_STATE_CD = "transactionStateCd";
    private static final String TRANSACTION_SUBTYPE_CD = "transactionSubtypeCd";
    private static final String TRANSACTION_TYPE_CD = "transactionTypeCd";
    private static final String UPD_USER_ID = "updUserId";

    private TestHelpers() {

    }

    public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
        GregorianCalendar entryDate = new GregorianCalendar();
        entryDate.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(entryDate);
    }

    public static EfilingService createBaseEfilingService() throws DatatypeConfigurationException {
        return new EfilingService(
                BigDecimal.TEN,
                BigDecimal.TEN,
                CLIENT_REFERENCE_TXT,
                COURT_FILE_NUMBER,
                DOCUMENTS_PROCESSED,
                getXmlDate(TestHelpers.DATE),
                "10",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                BigDecimal.TEN,
                null);
    }
    public static EfilingService createUpdateEfilingService(EfilingService service) throws DatatypeConfigurationException {
        service.setServiceId(BigDecimal.TEN);
        service.setServiceReceivedDateTime(getXmlDate(TestHelpers.DATE));
        service.setServiceReceivedDtmText(SERVICE_RECEIVED_DTM_TEXT);
        service.setServiceSessionId(BigDecimal.TEN);
        service.setServiceSubtypeCd(SERVICE_SUBTYPE_CD);
        service.setServiceTypeCd(SERVICE_TYPE_CD);
        service.setServiceTypeDesc(SERVICE_TYPE_DESC);
        service.setTransactions(Arrays.asList(createEfilingTransaction()));
        return service;
    }
    public static EfilingTransaction createEfilingTransaction() throws DatatypeConfigurationException {
        return new EfilingTransaction(
                APPROVAL_CD,
                BC_ONLINE_ACCOUNT_NO,
                BC_ONLINE_FEE_CODE_TXT,
                BC_ONLINE_RESPONSE_TYPE_TXT,
                BC_ONLINE_RETURN_CD,
                BC_ONLINE_RETURN_MESSAGE_TXT,
                BC_ONLINE_SEQUENCE_TXT,
                BigDecimal.TEN,
                BC_ONLINE_TRANSACTION_TYPE_CD,
                CREDIT_CARD_TYPE_CD,
                BigDecimal.TEN,
                getXmlDate(TestHelpers.DATE),
                ENT_USER_ID,
                INTERNAL_CLIENT_NO,
                INVOICE_NO,
                BigDecimal.TEN,
                getXmlDate(TestHelpers.DATE),
                BigDecimal.TEN,
                REFERENCE_MESSAGE_TXT,
                RESPONSE_CD,
                BigDecimal.TEN,
                SESSION_KEY_NO,
                TERMINAL_IDENTIFIER_NO,
                BigDecimal.TEN,
                TRANSACTION_STATE_CD,
                TRANSACTION_SUBTYPE_CD,
                TRANSACTION_TYPE_CD,
                getXmlDate(TestHelpers.DATE),
                getXmlDate(TestHelpers.DATE),
                UPD_USER_ID
        );
    }
    public static Service createService() throws DatatypeConfigurationException {
        Service service = new Service();
        service.setAccountId(BigDecimal.TEN);
        service.setClientId(BigDecimal.TEN);
        service.setClientReferenceTxt(TestHelpers.CLIENT_REFERENCE_TXT);
        service.setCourtFileNo(TestHelpers.COURT_FILE_NUMBER);
        service.setDocumentsProcessed(TestHelpers.DOCUMENTS_PROCESSED);
        service.setEntDtm(TestHelpers.getXmlDate(TestHelpers.DATE));
        service.setEntUserId("10");
        service.setServiceId(BigDecimal.TEN);
        service.setServiceReceivedDtm(TestHelpers.getXmlDate(TestHelpers.DATE));
        service.setServiceReceivedDtmText(TestHelpers.SERVICE_RECEIVED_DTM_TEXT);
        service.setServiceSessionId(BigDecimal.TEN);
        service.setServiceSubtypeCd(TestHelpers.SERVICE_SUBTYPE_CD);
        service.setServiceTypeCd(TestHelpers.SERVICE_TYPE_CD);
        service.setServiceTypeDesc(TestHelpers.SERVICE_TYPE_DESC);
        return service;
    }
}
