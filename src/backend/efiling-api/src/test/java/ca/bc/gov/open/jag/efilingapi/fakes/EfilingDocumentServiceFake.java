package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

import java.util.List;

public class EfilingDocumentServiceFake implements EfilingDocumentService {
    @Override
    public DocumentDetails getDocumentTypeDetails(String courtLevel, String courtClass, String documentType) {
        return null;
    }

    @Override
    public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
        return null;
    }
}
