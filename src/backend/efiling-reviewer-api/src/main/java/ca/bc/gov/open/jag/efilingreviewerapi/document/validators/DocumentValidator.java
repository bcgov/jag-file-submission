package ca.bc.gov.open.jag.efilingreviewerapi.document.validators;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentValidator {

    void validateDocument(String documentType, MultipartFile file);

    void validateExtractedDocument(String documentType, List<DiligenAnswerField> answers);

}
