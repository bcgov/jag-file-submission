package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingStatsService;

public class EfilingStatsServiceDemoImpl implements EfilingStatsService {
    @Override
    public DocumentDetails getDocumentDetails(String serviceId) {
        return new DocumentDetails("This is a doc");
    }
}
