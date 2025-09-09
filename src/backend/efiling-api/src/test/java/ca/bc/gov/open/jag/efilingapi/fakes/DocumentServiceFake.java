package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;

import java.util.List;

public class DocumentServiceFake implements DocumentService {
    @Override
    public List<DocumentTypeDetails> getValidDocumentTypes(GetValidDocumentTypesRequest getValidDocumentTypesRequest) {
        return null;
    }
}
