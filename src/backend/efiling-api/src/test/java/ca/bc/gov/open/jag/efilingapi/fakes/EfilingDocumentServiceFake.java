package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

import java.util.List;

public class EfilingDocumentServiceFake implements EfilingDocumentService {
    @Override
    public DocumentDetails getDocumentTypeDetails(String courtLevel, String courtClass, String documentType) {
        return null;
    }

    @Override
    public List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass) {
        return null;
    }
}
