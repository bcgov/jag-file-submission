package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class RushDocumentRequest {
    private BigDecimal procReqId;
    private BigDecimal procItemSeqNo;
    private BigDecimal docSeqNo;

    public RushDocumentRequest(RushDocumentRequest.Builder builder) {
        this.procReqId = builder.procReqId;
        this.procItemSeqNo = builder.procItemSeqNo;
        this.docSeqNo = builder.docSeqNo;
    }

    public BigDecimal getProcReqId() {  return procReqId; }

    public BigDecimal getProcItemSeqNo() { return procItemSeqNo; }

    public BigDecimal getDocSeqNo() { return docSeqNo; }

    public static RushDocumentRequest.Builder builder() {
        return new RushDocumentRequest.Builder();
    }

    public static class Builder {

        private BigDecimal procReqId;
        private BigDecimal procItemSeqNo;
        private BigDecimal docSeqNo;

        public RushDocumentRequest.Builder procReqId(BigDecimal procReqId) { this.procReqId = procReqId; return this;}
        public RushDocumentRequest.Builder procItemSeqNo(BigDecimal procItemSeqNo) { this.procItemSeqNo = procItemSeqNo; return this;}
        public RushDocumentRequest.Builder docSeqNo(BigDecimal docSeqNo) { this.docSeqNo = docSeqNo; return this;}

        public RushDocumentRequest create() {
            return new RushDocumentRequest(this);
        }

    }
}
