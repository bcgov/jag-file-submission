package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExtractStore {

    //Request Cache
    Optional<ExtractRequest> put(BigDecimal id, ExtractRequest documentExtractRequest);

    Optional<ExtractRequest> get(BigDecimal id);

    void evict(BigDecimal id);

    //Response Cache
    Optional<ExtractResponse> put(BigDecimal id, ExtractResponse documentExtractResponse);

    Optional<ExtractResponse> getResponse(BigDecimal id);

    void evictResponse(BigDecimal id);

}
