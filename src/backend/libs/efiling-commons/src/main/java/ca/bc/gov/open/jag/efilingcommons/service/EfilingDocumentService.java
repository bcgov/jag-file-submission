package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;

import java.util.List;

public interface EfilingDocumentService {

    /**
     * Search for details related to the document type
     * @param courtLevel level code used by cso
     * @param courtClass class code used by cso
     * @param documentType type code used by cso
     * @return All details related to request document type
     */
    DocumentTypeDetails getDocumentTypeDetails(String courtLevel, String courtClass, String documentType, String division);

    /**
     * Search for are document types(with details) by parameters
     * @param courtLevel level code used by cso
     * @param courtClass class code used by cso
     * @return list of document types
     */
    List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass, String division);

}
