package ca.bc.gov.open.jag.efilingreviewerapi.document.validators;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidation;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface DocumentValidator {

    void validateDocument(String documentType, MultipartFile file);

    DocumentValidation validateExtractedDocument(BigDecimal documentId, DocumentTypeConfiguration documentTypeConfiguration, List<DiligenAnswerField> answers);

}
