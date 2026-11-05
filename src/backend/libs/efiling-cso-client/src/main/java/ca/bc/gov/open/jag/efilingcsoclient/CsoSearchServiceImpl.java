package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.courts.appeal.ws.services.COACase;
import ca.bc.gov.courts.appeal.ws.services.CSOSearchSoap;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsoSearchServiceImpl implements EfilingSearchService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CSOSearchSoap csoSearchSoap;

    public CsoSearchServiceImpl(CSOSearchSoap csoSearchSoap) {
        this.csoSearchSoap = csoSearchSoap;
    }

    @Override
    public boolean caseNumberExists(String caseNumber) {

        logger.info("search for case number request received");

        if (StringUtils.isBlank(caseNumber)) throw new IllegalArgumentException("caseNumber is required.");

        COACase result = csoSearchSoap.searchByCaseNumber(caseNumber);

        return (result != null);

    }
}
