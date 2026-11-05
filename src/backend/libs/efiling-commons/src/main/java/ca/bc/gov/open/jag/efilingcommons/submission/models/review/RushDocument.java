package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class RushDocument {
    protected String clientFileNm;
    protected DateTime entDtm;
    protected String entUserId;
    protected String fileServer;
    protected String objectGuid;
    protected BigDecimal processItemSeqNo;
    protected BigDecimal processRequestId;
    protected BigDecimal processSupportDocSeqNo;
    protected String tempFileName;
    protected DateTime updDtm;
    protected String updUserId;

    public RushDocument() {}

    public String getClientFileNm() {
        return clientFileNm;
    }

    public void setClientFileNm(String clientFileNm) {
        this.clientFileNm = clientFileNm;
    }

    public DateTime getEntDtm() {
        return entDtm;
    }

    public void setEntDtm(DateTime entDtm) {
        this.entDtm = entDtm;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public BigDecimal getProcessItemSeqNo() {
        return processItemSeqNo;
    }

    public void setProcessItemSeqNo(BigDecimal processItemSeqNo) {
        this.processItemSeqNo = processItemSeqNo;
    }

    public BigDecimal getProcessRequestId() {
        return processRequestId;
    }

    public void setProcessRequestId(BigDecimal processRequestId) {
        this.processRequestId = processRequestId;
    }

    public BigDecimal getProcessSupportDocSeqNo() {
        return processSupportDocSeqNo;
    }

    public void setProcessSupportDocSeqNo(BigDecimal processSupportDocSeqNo) {
        this.processSupportDocSeqNo = processSupportDocSeqNo;
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public DateTime getUpdDtm() {
        return updDtm;
    }

    public void setUpdDtm(DateTime updDtm) {
        this.updDtm = updDtm;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }
}
