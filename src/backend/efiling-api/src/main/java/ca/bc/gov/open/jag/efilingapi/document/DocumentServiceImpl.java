package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

import java.util.List;

public class DocumentServiceImpl implements DocumentService {

    private final EfilingDocumentService efilingDocumentService;

    public DocumentServiceImpl(EfilingDocumentService efilingDocumentService) {
        this.efilingDocumentService = efilingDocumentService;
    }

    @Override
    public List<DocumentType> getValidDocumentTypes(GetValidDocumentTypesRequest getValidDocumentTypesRequest) {
        return efilingDocumentService.getDocumentTypes(getValidDocumentTypesRequest.getCourtLevel(), getValidDocumentTypesRequest.getCourtClassification());
    }

}
