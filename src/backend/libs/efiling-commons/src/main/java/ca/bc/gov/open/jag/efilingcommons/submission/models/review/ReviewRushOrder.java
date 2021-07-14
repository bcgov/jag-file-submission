package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class ReviewRushOrder {
    protected DateTime courtOrderDt;
    protected BigDecimal packageId;
    protected String rushFilingReasonTxt;
    protected List<RushDocument> supportDocs;

    public ReviewRushOrder() {}

    public DateTime getCourtOrderDt() {
        return courtOrderDt;
    }

    public void setCourtOrderDt(DateTime courtOrderDt) {
        this.courtOrderDt = courtOrderDt;
    }

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public String getRushFilingReasonTxt() {
        return rushFilingReasonTxt;
    }

    public void setRushFilingReasonTxt(String rushFilingReasonTxt) {
        this.rushFilingReasonTxt = rushFilingReasonTxt;
    }

    public List<RushDocument> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(List<RushDocument> supportDocs) {
        this.supportDocs = supportDocs;
    }
}
