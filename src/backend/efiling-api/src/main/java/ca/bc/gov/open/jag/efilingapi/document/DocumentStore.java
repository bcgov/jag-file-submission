package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;

import java.util.List;
import java.util.UUID;

public interface DocumentStore {

    byte[] put(String compositeId, byte[] content);

    byte[] get(String compositeId);

    void evict(String compositeId);

    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

    List<DocumentType> getDocumentTypes(String courtLevel, String courtClass);
}
