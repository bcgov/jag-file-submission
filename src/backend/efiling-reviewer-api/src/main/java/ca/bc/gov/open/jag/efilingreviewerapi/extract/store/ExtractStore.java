package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExtractStore {

    Optional<ExtractRequest> put(BigDecimal id, ExtractRequest documentExtractRequest);

    Optional<ExtractRequest> get(BigDecimal id);

    Optional<ExtractResponse> put(BigDecimal id, ExtractResponse documentExtractResponse);

}
