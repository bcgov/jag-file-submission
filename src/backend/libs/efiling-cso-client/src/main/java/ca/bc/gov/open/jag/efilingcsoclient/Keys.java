package ca.bc.gov.open.jag.efilingcsoclient;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Keys {

    private Keys() {}

    public static final String INVOICE_PREFIX = "15";
    public static final String TRANSACTION_TYPE_CD = "12";
    public static final String TRANSACTION_SUB_TYPE_CD = "BNST";
    public static final String RUSH_PROCESS_REASON_CD = "PRO";
    public static final String REQUEST_PROCESS_STATUS_CD = "RQST";
    public static final String APPROVED_PROCESS_STATUS_CD = "APP";
    public static final String SERVICE_SUBTYPE_CD = "FSVC";
    public static final String SERVICE_TYPE_CD = "DCFL";
    public static final String SUBMISSION_UPLOAD_STATE_CD = "CMPL";
    public static final String SUBMISSION_DOCUMENT_STATUS_TYPE_CD = "SUB";
    public static final String NOT_REQUIRED_PAYMENT_STATUS_CD = "NREQ";
    public static final String NOT_PROCESSED_PAYMENT_STATUS_CD = "NOPR";
    public static final String XML_DOCUMENT_INSTANCE_YN = "false";
    public static final String DOCUMENT_SUB_TYPE_CD = "ODOC";
    public static final String INDIVIDUAL_ROLE_TYPE_CD = "IND";
    public static final String ORGANIZATION_ROLE_TYPE_CD = "ORG";
    public static final String PARTY_TYPE_CD = "CLA";
    public static final String PARTY_NAME_TYPE_CD = "CUR";
    public static final String PRIVILEGE_CD = "UPDT";
    public static final String SUBMISSION_REPORT_NAME = "submission";
    public static final String RECEIPT_REPORT_NAME = "receipt";
    public static final String REGISTRY_NOTICE_NAME = "notice";
    public static final String SUBMISSION_REPORT_PARAMETER = "prm_package_id";
    public static final String PARAM_REPORT_PARAMETER = "param1";
    public static final Map<String, BigDecimal> IDENTITY_PROVIDERS = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>( "BCEID", BigDecimal.ONE ),
                new AbstractMap.SimpleImmutableEntry<>( "BCSC",  new BigDecimal(2) ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    public static final String VIEW_ALL_PACKAGE_SUBPATH = "accounts/filingStatus.do?actionType=filterStatus&useFilter=ALL";
    public static final String CSO_USER_ROLE_FILE = "FILE";
    public static final String CSO_USER_ROLE_CAEF = "CAEF";
    public static final String CSO_ACTUAL_SUBMITTED_DATE = "ASUB";
    public static final String CSO_CALCULATED_SUBMITTED_DATE = "CSUB";

    public static final Map<String, String> RUSH_TYPES = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>( "PRO", "PRO" ),
            new AbstractMap.SimpleImmutableEntry<>( "RULE",  "CRTR" ),
            new AbstractMap.SimpleImmutableEntry<>( "COURT",  "CRTD" ),
            new AbstractMap.SimpleImmutableEntry<>( "OTHER",  "OTHR" ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


    public static final String CSO_DOCUMENT_REJECTED = "REJ";
    public static final String CSO_DOCUMENT_COURTESY_CORRECTED = "CCOR";
    public static final String CSO_DOCUMENT_SUBMITTED = "SUB";
    public static final String CSO_DOCUMENT_RE_SUBMITTED = "RSUB";
    public static final String CSO_DOCUMENT_REFERRED = "REF";
    public static final String PACKAGE_STATUS_PENDING = "Pending";
    public static final String PACKAGE_STATUS_ACTION_REQUIRED = "Action Required";
    public static final String PACKAGE_STATUS_COMPLETE = "Complete";

    public static final String AUTO_PROCESSING_STATE = "AUTO";
    public static final String MANUAL_PROCESSING_STATE = "MANUAL";

    public static final String OTHER_DOCUMENT_TYPE = "OTH";

}
