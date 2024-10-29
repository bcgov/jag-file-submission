package ca.bc.gov.open.jag.efilingapi.lookup;

import ca.bc.gov.open.jag.efilingapi.api.CountriesApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CountryCode;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountriesApiDelegateImpl implements CountriesApiDelegate {

    Logger logger = LoggerFactory.getLogger(CountriesApiDelegateImpl.class);

    private final EfilingLookupService efilingLookupService;

    public CountriesApiDelegateImpl(EfilingLookupService efilingLookupService) {
        this.efilingLookupService = efilingLookupService;
    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<List<CountryCode>> getCountries() {

        logger.info("Get countries list request received");

        return ResponseEntity.ok(efilingLookupService.getCountries().stream()
            .map(this::createCountryCode)
            .collect(Collectors.toList()));
    }

    private CountryCode createCountryCode(LookupItem item) {
        CountryCode countryCode = new CountryCode();
        countryCode.setCode(item.getCode());
        countryCode.setDescription(item.getDescription());
        return countryCode;
    }
}
