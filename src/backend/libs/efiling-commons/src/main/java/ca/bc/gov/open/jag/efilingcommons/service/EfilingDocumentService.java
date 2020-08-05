package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;

import java.util.List;

public interface EfilingDocumentService {
    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

    List<DocumentType> getDocumentTypes(String courtLevel, String courtClass);
}
