package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;

import java.util.List;

public interface DocumentService {

    /**
     * Returns a list of valid document types
     * @param getValidDocumentTypesRequest
     * @return
     */
    List<DocumentType> getValidDocumentTypes(GetValidDocumentTypesRequest getValidDocumentTypesRequest);

}
