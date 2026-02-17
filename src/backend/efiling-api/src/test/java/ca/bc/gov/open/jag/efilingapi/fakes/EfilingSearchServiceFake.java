package ca.bc.gov.open.jag.efilingapi.fakes;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;

public class EfilingSearchServiceFake implements EfilingSearchService {
    @Override
    public boolean caseNumberExists(String caseNumber) {
        return false;
    }
}
