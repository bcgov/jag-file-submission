package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class DeleteSubmissionDocumentRequest extends FilingPackageRequest {
    private String documentId;

    public DeleteSubmissionDocumentRequest(BigDecimal clientId, BigDecimal packageNo, String documentId) {
        super(clientId, packageNo);
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
