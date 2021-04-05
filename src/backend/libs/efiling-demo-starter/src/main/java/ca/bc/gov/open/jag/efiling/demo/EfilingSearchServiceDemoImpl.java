package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;

public class EfilingSearchServiceDemoImpl implements EfilingSearchService {

    @Override
    public boolean caseNumberExists(String caseNumber) {
        return true;
    }
}
