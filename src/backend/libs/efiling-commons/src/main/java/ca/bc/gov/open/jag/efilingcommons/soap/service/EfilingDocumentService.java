package ca.bc.gov.open.jag.efilingcommons.soap.service;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;

public interface EfilingDocumentService {
    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);
}
