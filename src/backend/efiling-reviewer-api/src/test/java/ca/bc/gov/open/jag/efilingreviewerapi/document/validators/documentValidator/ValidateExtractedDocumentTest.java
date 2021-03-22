package ca.bc.gov.open.jag.efilingreviewerapi.document.validators.documentValidator;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidatorImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentTypeMismatchException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerRestrictedDocumentException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsValidatorImpl test suite")
public class ValidateExtractedDocumentTest {

    public static final String DOCUMENT_TYPE = "RCC";
    public static final String RESPONSE_TO_CIVIL_CLAIM = "Response to Civil Claim";
    public static final String NOT_RESPONSE_TO_CIVIL_CLAIM = "THIS IS NOT VALID";
    public static final String RESTRICTED_DOCUMENT = "This is a temporary";
    public static final int NOT_DOCUMENT_TYPE_ID = 123;
    public static final String THE_VALUE = "THIS IS A VALUE";
    DocumentValidatorImpl sut;

    @Mock
    DiligenService diligenServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.doNothing().when(diligenServiceMock).deleteDocument(ArgumentMatchers.eq(BigDecimal.ONE));

        sut = new DocumentValidatorImpl(null, diligenServiceMock);

    }

    @Test
    @DisplayName("Ok: executes with no exception")
    public void withValidDocumentExecuteWithoutThrowing() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(answerField);

        Assertions.assertDoesNotThrow(() -> sut.validateExtractedDocument(BigDecimal.ZERO ,DOCUMENT_TYPE, answers));

    }

    @Test
    @DisplayName("Error: throws exception when no document type found")
    public void withInValidDocumentTypeThrowException() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(THE_VALUE))
                .create();

        answers.add(answerField);

        Assertions.assertThrows(AiReviewerDocumentTypeMismatchException.class, () -> sut.validateExtractedDocument(BigDecimal.ZERO ,DOCUMENT_TYPE, answers));

    }

    @Test
    @DisplayName("Error: throws exception when mismatched document type found")
    public void withInValidDocumentThrowException() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(NOT_RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(answerField);

        Assertions.assertThrows(AiReviewerDocumentTypeMismatchException.class, () -> sut.validateExtractedDocument(BigDecimal.ZERO ,DOCUMENT_TYPE, answers));

    }

    @Test
    @DisplayName("Error: throws exception when restricted document type found")
    public void withRestrictedDocumentThrowException() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESTRICTED_DOCUMENT))
                .create();

        answers.add(answerField);

        Assertions.assertThrows(AiReviewerRestrictedDocumentException.class, () -> sut.validateExtractedDocument(BigDecimal.ONE ,DOCUMENT_TYPE, answers));

    }
}
