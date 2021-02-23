package ca.bc.gov.open.efilingdiligenclient.diligen.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DiligenResponse {
    @JsonProperty("data")
    private DiligenData data;

    public DiligenData getData() {
        return data;
    }

    public void setData(DiligenData data) {
        this.data = data;
    }
}
