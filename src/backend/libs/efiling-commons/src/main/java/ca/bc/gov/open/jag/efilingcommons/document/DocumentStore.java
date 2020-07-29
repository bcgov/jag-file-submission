package ca.bc.gov.open.jag.efilingcommons.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;

public interface DocumentStore {

    byte[] put(String compositeId, byte[] content);

    byte[] get(String compositeId);

    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

}
