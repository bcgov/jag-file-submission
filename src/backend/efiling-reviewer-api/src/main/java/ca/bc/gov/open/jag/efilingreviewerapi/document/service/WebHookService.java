package ca.bc.gov.open.jag.efilingreviewerapi.document.service;

import java.math.BigDecimal;

public interface WebHookService {
    void sendDocumentReady(BigDecimal documentId, String documentType);
}
