package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;

import java.util.List;

public class DocumentServiceFake implements DocumentService {
    @Override
    public List<DocumentType> getValidDocumentTypes(GetValidDocumentTypesRequest getValidDocumentTypesRequest) {
        return null;
    }
}
