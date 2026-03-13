package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

import java.util.List;

public class DocumentServiceImpl implements DocumentService {

    private final EfilingDocumentService efilingDocumentService;

    public DocumentServiceImpl(EfilingDocumentService efilingDocumentService) {
        this.efilingDocumentService = efilingDocumentService;
    }

    @Override
    public List<DocumentTypeDetails> getValidDocumentTypes(GetValidDocumentTypesRequest getValidDocumentTypesRequest) {
        return efilingDocumentService.getDocumentTypes(getValidDocumentTypesRequest.getCourtLevel(), getValidDocumentTypesRequest.getCourtClassification(), Keys.DEFAULT_DIVISION);
    }

}
