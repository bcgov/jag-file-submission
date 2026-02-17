package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

public class PackageLinks {

    private String packageHistoryUrl;

    public String getPackageHistoryUrl() {
        return packageHistoryUrl;
    }

    public PackageLinks(String packageHistoryUrl) {
        this.packageHistoryUrl = packageHistoryUrl;
    }

    public PackageLinks(Builder builder) {
        this.packageHistoryUrl = builder.packageHistoryUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String packageHistoryUrl;

        public Builder packageHistoryUrl(String packageHistoryUrl) {
            this.packageHistoryUrl = packageHistoryUrl;
            return this;
        }

        public PackageLinks create() {
            return new PackageLinks(this);
        }

    }

}
