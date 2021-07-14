package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class ReviewRushOrder {
    protected DateTime courtOrderDt;
    protected BigDecimal packageId;
    protected String rushFilingReasonTxt;
    protected List<RushDocument> supportDocs;
}
