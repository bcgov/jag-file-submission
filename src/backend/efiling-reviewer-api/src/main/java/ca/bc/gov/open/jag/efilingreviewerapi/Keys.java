package ca.bc.gov.open.jag.efilingreviewerapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Keys {

    private Keys() {}

    public static final String PROCESSED_STATUS = "PROCESSED";
    public static final String DOCUMENT_TYPE = "document-type";
    public static final List<String> ACCEPTED_STATUS = Arrays.asList("QUEUED_FOR_ML_ANALYSIS", "DONE_OCR_PROCESSING","QUEUED_FOR_OCR_PROCESSING", "QUEUED_FOR_TRANSLATE", "QUEUED_FOR_PROCESSING");
    public static final Map<String, String> ACCEPTED_DOCUMENT_TYPES = new HashMap<String, String>() {{
        put("RCC", "Response to Civil Claim");
    }};
    public static final Map<String, String> RESTRICTED_DOCUMENT_TYPES = new HashMap<String, String>() {{
        put("TEST", "This is a temporary");
    }};
    public static final Integer ANSWER_DOCUMENT_TYPE_ID = 232;

}
