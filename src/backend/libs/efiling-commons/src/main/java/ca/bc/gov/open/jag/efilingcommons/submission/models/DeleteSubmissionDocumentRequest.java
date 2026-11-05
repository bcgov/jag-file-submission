package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class DeleteSubmissionDocumentRequest extends FilingPackageRequest {
    private String documentId;

    public DeleteSubmissionDocumentRequest(BigDecimal clientId, BigDecimal packageNo, String parentApplication, String documentId) {
        super(clientId, null, packageNo, parentApplication);
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
