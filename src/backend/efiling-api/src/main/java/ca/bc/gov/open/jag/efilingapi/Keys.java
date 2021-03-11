package ca.bc.gov.open.jag.efilingapi;

public class Keys {

    protected Keys() {}

    public static final String EFILING_APP = "efiling";

    public static final String EFILING_API_NAME = "efiling-api";

    public static final String MDC_EFILING_REQUEST_HEADERS = EFILING_APP + ".headers";

    public static final String MDC_EFILING_JWT = EFILING_APP + ".jwt";

    public static final String MDC_EFILING_SUBMISSION_ID =  EFILING_APP + ".submissionId";

    public static final String UNIVERSAL_ID_CLAIM_KEY = "universal-id";

    public static final String CSO_APPLICATION_CLAIM_KEY = "cso-application-code";

    public static final String IDENTITY_PROVIDER_CLAIM_KEY = "identityProviderAlias";

    public static final String EFILING_CLIENT_ROLE = "efiling-client";

    public static final String EFILING_USER_ROLE = "efiling-user";

    public static final String EFILING_PAYMENT_RECEIPT_FILENAME = "payment-receipt.pdf";

    public static final String EFILING_SUBMISSION_SHEET_FILENAME = "submission-sheet.pdf";

}
