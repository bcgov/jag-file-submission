package ca.bc.gov.open.jag.efilingapi.court.models;

public class GetCourtDetailsRequest {

    private String courtLocation;
    private String courtLevel;
    private String courtClassification;

    public String getCourtLocation() {
        return courtLocation;
    }

    public String getCourtLevel() {
        return courtLevel;
    }

    public String getCourtClassification() {
        return courtClassification;
    }
    
    public GetCourtDetailsRequest(Builder builder) {
        this.courtClassification = builder.courtClassification;
        this.courtLevel = builder.courtLevel;
        this.courtLocation = builder.courtLocation;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {

        private String courtLocation;
        private String courtClassification;
        private String courtLevel;
        
        public Builder courtLocation(String courtLocation) {
            this.courtLocation = courtLocation;
            return this;
        }
        
        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }
        
        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }
        
        public GetCourtDetailsRequest create() {
            return new GetCourtDetailsRequest(this);
        }

    }
    
    
}
