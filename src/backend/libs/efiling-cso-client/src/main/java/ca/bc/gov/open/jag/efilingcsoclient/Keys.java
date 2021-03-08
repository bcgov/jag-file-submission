package ca.bc.gov.open.jag.efilingcsoclient;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Keys {

    private Keys() {}

    public static final String INVOICE_PREFIX = "15";
    public static final String TRANSACTION_STATE_DECLINED = "DEC";
    public static final String TRANSACTION_STATE_APPROVED = "APP";
    public static final String TRANSACTION_TYPE_CD = "12";
    public static final String TRANSACTION_SUB_TYPE_CD = "BNST";
    public static final String RUSH_PROCESS_REASON_CD = "PRO";
    public static final String REQUEST_PROCESS_STATUS_CD = "RQST";
    public static final String APPROVED_PROCESS_STATUS_CD = "APP";
    public static final String SERVICE_SUBTYPE_CD = "FSVC";
    public static final String SERVICE_TYPE_CD = "DCFL";
    public static final String SUBMISSION_UPLOAD_STATE_CD = "CMPL";
    public static final String SUBMISSION_DOCUMENT_STATUS_TYPE_CD = "SUB";
    public static final String PAYMENT_STATUS_CD = "NREQ";
    public static final String XML_DOCUMENT_INSTANCE_YN = "false";
    public static final String DOCUMENT_SUB_TYPE_CD = "ODOC";
    public static final String PARTY_ROLE_TYPE_CD = "IND";
    public static final String PARTY_TYPE_CD = "CLA";
    public static final String PARTY_NAME_TYPE_CD = "CUR";
    public static final String PRIVILEGE_CD = "UPDT";
    public static final String SUBMISSION_REPORT_NAME = "submission";
    public static final String RECEIPT_REPORT_NAME = "receipt";
    public static final String SUBMISSION_REPORT_PARAMETER = "prm_package_id";
    public static final String PARAM_REPORT_PARAMETER = "param1";
    public static final Map<String, BigDecimal> IDENTITY_PROVIDERS = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>( "BCEID", BigDecimal.ONE ),
                new AbstractMap.SimpleImmutableEntry<>( "BCSC",  new BigDecimal(2) ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    public static final String VIEW_ALL_PACKAGE_SUBPATH = "filing/status/display.do?actionType=filterStatus&useFilter=ALL";
    public static final String CSO_USER_ROLE_VIND = "VIND";
    public static final String CSO_USER_ROLE_FILE = "FILE";
    public static final String CSO_USER_ROLE_CAEF = "CAEF";
    public static final String CSO_USER_ROLE_IND = "IND";
    public static final String BCSC_IDENTITY_PROVIDER = "BCSC";


}
