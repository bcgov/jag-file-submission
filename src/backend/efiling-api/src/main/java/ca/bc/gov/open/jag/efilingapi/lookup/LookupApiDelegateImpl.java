package ca.bc.gov.open.jag.efilingapi.lookup;

import ca.bc.gov.open.jag.efilingapi.api.LookupApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtInformation;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentTypes;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.stream.Collectors;
@Service
public class LookupApiDelegateImpl implements LookupApiDelegate {
    Logger logger = LoggerFactory.getLogger(LookupApiDelegateImpl.class);

    private final DocumentStore documentStore;

    public LookupApiDelegateImpl(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }


    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<DocumentTypes> getDocumentTypes(String courtLevel, String courtClass) {

        try {
            DocumentTypes result = new DocumentTypes();
            result.setDocumentTypes(documentStore.getDocumentTypes(courtLevel, courtClass).stream()
                    .map(documentType -> toDocumentType(documentType)).collect(Collectors.toList()));
            return ResponseEntity.ok(result);
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            EfilingError response = new EfilingError();
            response.setError(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode());
            response.setMessage(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CourtInformation> getCourtInformation(String courtName) {
        CourtInformation courtInformation = new CourtInformation();
        courtInformation.setCourtId(BigDecimal.ONE);
        courtInformation.setCourtName("Test");
        courtInformation.setCourtCode("Test");
        courtInformation.setTimeZone("Test");
        courtInformation.setDaylightSavings("Test");
        courtInformation.setCourtIdentifierCode("Test");
        courtInformation.setAddressLine1("Test");
        courtInformation.setAddressLine2("Test");
        courtInformation.setAddressLine3("Test");
        courtInformation.setPostalCode("Test");
        courtInformation.setCitySequenceNumber(BigDecimal.valueOf(123));
        courtInformation.setCityAbbreviation("Test");
        courtInformation.setCityName("Test");
        courtInformation.setProvinceSequenceNumber(BigDecimal.valueOf(123));
        courtInformation.setProvinceAbbreviation("Test");
        courtInformation.setProvinceName("Test");
        courtInformation.setCountryId(BigDecimal.ONE);
        courtInformation.setCountryAbbreviation("Test");
        courtInformation.setCountryName("Test");
        courtInformation.setIsProvincialCourt(true);
        courtInformation.setIsSupremeCourt(true);
        return ResponseEntity.ok(courtInformation);
    }

    private DocumentType toDocumentType(ca.bc.gov.open.jag.efilingcommons.model.DocumentType documentType) {
        DocumentType outDocumentType = new DocumentType();
        outDocumentType.setType(documentType.getType());
        outDocumentType.setDescription(documentType.getDescription());
        return outDocumentType;
    }
}
