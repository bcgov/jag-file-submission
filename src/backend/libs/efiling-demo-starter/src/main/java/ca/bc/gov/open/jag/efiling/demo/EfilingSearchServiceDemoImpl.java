package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;
import org.apache.commons.lang3.StringUtils;

public class EfilingSearchServiceDemoImpl implements EfilingSearchService {

    @Override
    public boolean caseNumberExists(String caseNumber) {
        return StringUtils.equals("1", caseNumber);
    }
}
