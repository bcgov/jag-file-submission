package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;

public class EfilingDocumentServiceDemoImpl implements EfilingDocumentService {
    @Override
    public DocumentDetails getDocumentDetails(String documentType) {
        return new DocumentDetails("This is a doc");
    }
}
