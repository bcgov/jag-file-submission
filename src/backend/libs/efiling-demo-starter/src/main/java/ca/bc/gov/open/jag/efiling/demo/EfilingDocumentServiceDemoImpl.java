package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class EfilingDocumentServiceDemoImpl implements EfilingDocumentService {
    @Override
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return new DocumentDetails("This is a doc", BigDecimal.valueOf(7), true, true);
    }

    @Override
    public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
        return Arrays.asList(new DocumentType("Description1", "AFF", false), new DocumentType("Description2", "Type2", true));
    }
}
