package ca.bc.gov.open.jag.efilingreviewerapi;

import java.util.Arrays;
import java.util.List;

public class Keys {
    private Keys() {}

    public static final String PROCESSED_STATUS = "PROCESSED";
    public static final List<String> ACCEPTED_STATUS = Arrays.asList("QUEUED_FOR_ML_ANALYSIS", "DONE_OCR_PROCESSING","QUEUED_FOR_OCR_PROCESSING", "QUEUED_FOR_PROCESSING");

}
