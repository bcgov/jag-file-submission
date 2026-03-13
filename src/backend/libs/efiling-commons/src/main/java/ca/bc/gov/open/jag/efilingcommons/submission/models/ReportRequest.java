package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class ReportRequest {

    ReportsTypes report;
    BigDecimal packageId;
    String fileName;
    String universalId;

    public ReportsTypes getReport() { return report; }

    public BigDecimal getPackageId() { return packageId; }

    public String getFileName() { return fileName; }

    public String getUniversalId() { return universalId; }

    public void setUniversalId(String universalId) { this.universalId = universalId; }

    protected ReportRequest(ReportRequest.Builder builder) {

        this.report = builder.report;
        this.packageId = builder.packageId;
        this.fileName = builder.fileName;
        this.universalId = builder.universalId;

    }

    public static ReportRequest.Builder builder() {

        return new ReportRequest.Builder();
    }

    public static class Builder {

        private ReportsTypes report;
        private BigDecimal packageId;
        private String fileName;
        private String universalId;

        public ReportRequest.Builder report(ReportsTypes report) {
            this.report = report;
            return this;
        }

        public ReportRequest.Builder packageId(BigDecimal packageId) {
            this.packageId = packageId;
            return this;
        }

        public ReportRequest.Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ReportRequest.Builder universalId(String universalId) {
            this.universalId = universalId;
            return this;
        }

        public ReportRequest create() {
            return new ReportRequest(this);
        }
    }

}
