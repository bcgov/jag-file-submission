package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;

public interface EfilingDocumentService {
    DocumentDetails getDocumentDetails(String documentType);
}
