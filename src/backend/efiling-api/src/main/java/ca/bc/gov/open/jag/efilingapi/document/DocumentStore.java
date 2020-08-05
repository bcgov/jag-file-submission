package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;

import java.util.List;

public interface DocumentStore {

    byte[] put(String compositeId, byte[] content);

    byte[] get(String compositeId);

    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

    List<DocumentType> getDocumentTypes(String courtLevel, String courtClass);
}
