package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import org.slf4j.MDC;

import java.util.UUID;

public class MdcUtils {

    private MdcUtils() {}

    public static void setClientMDC(UUID submissionId, UUID transactionId) {
        MDC.put(Keys.MDC_EFILING_CLIENT_ID, SecurityUtils.getClientId());
        MDC.put(Keys.MDC_EFILING_SUBMISSION_ID, submissionId.toString());
        MDC.put(Keys.MDC_EFILING_TRANSACTION_ID, transactionId.toString());
    }

    public static void clearClientMDC() {
        MDC.remove(Keys.MDC_EFILING_CLIENT_ID);
        MDC.remove(Keys.MDC_EFILING_SUBMISSION_ID);
        MDC.remove(Keys.MDC_EFILING_TRANSACTION_ID);
    }

    public static void setUserMDC(UUID submissionId, UUID transactionId) {
        MDC.put(Keys.MDC_EFILING_UNIVERSAL_ID,
                SecurityUtils.getUniversalIdFromContext().isPresent() ? SecurityUtils.getUniversalIdFromContext().get().toString() : "");
        MDC.put(Keys.MDC_EFILING_SUBMISSION_ID, submissionId.toString());
        MDC.put(Keys.MDC_EFILING_TRANSACTION_ID, transactionId.toString());
        MDC.put(Keys.MDC_EFILING_IDENTITY_PROVIDER, SecurityUtils.getOtherClaim(Keys.IDENTITY_PROVIDER_MAPPING).orElse(""));
    }

    public static void clearUserMDC() {
        MDC.remove(Keys.MDC_EFILING_UNIVERSAL_ID);
        MDC.remove(Keys.MDC_EFILING_SUBMISSION_ID);
        MDC.remove(Keys.MDC_EFILING_TRANSACTION_ID);
        MDC.remove(Keys.MDC_EFILING_IDENTITY_PROVIDER);
    }

}
