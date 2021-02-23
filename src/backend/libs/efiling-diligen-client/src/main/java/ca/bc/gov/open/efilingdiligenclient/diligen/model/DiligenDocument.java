package ca.bc.gov.open.efilingdiligenclient.diligen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class DiligenDocument {

    @JsonProperty("file_id")
    private BigDecimal fileId;

    public BigDecimal getFileId() {
        return fileId;
    }

    public void setFileId(BigDecimal fileId) {
        this.fileId = fileId;
    }

}
