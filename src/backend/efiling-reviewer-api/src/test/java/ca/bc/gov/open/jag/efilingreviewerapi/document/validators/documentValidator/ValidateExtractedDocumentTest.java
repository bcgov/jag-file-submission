package ca.bc.gov.open.jag.efilingreviewerapi.document.validators.documentValidator;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidation;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.ValidationTypes;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidatorImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerRestrictedDocumentException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsValidatorImpl test suite")
public class ValidateExtractedDocumentTest {

    private static final String RESPONSE_TO_CIVIL_CLAIM = "Response to Civil Claim";
    private static final String NOT_RESPONSE_TO_CIVIL_CLAIM = "THIS IS NOT VALID";
    private static final String RESTRICTED_DOCUMENT = "This is a temporary";
    private static final int NOT_DOCUMENT_TYPE_ID = 123;
    private static final String THE_VALUE = "THIS IS A VALUE";
    private static final String PLAINTIFF = "PLAINTIFF";
    private static final String DEFENDANT = "DEFENDANT";
    private static final DocumentTypeConfiguration DOCUMENT_TYPE_CONFIGURATION = DocumentTypeConfiguration
            .builder()
            .documentType("RCC")
            .documentTypeDescription("Response to Civil Claim")
            .projectId(2)
            .create();


    DocumentValidatorImpl sut;

    @Mock
    DiligenService diligenServiceMock;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.doNothing().when(diligenServiceMock).deleteDocument(ArgumentMatchers.eq(BigDecimal.ONE));

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(RESTRICTED_DOCUMENT))).thenReturn(true);

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(RESPONSE_TO_CIVIL_CLAIM))).thenReturn(false);

        Mockito.when(restrictedDocumentRepositoryMock.existsByDocumentTypeDescription(ArgumentMatchers.eq(NOT_RESPONSE_TO_CIVIL_CLAIM))).thenReturn(false);

        sut = new DocumentValidatorImpl(null, diligenServiceMock, null, restrictedDocumentRepositoryMock);

    }

    @Test
    @DisplayName("Ok: executes with no exception")
    public void withValidDocumentExecuteWithoutThrowing() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField documentAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(documentAnswerField);

        DiligenAnswerField plaintiffAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_PLAINTIFF_ID)
                .values(Collections.singletonList(PLAINTIFF))
                .create();

        answers.add(plaintiffAnswerField);

        DiligenAnswerField defendantAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DEFENDANT_ID)
                .values(Collections.singletonList(DEFENDANT))
                .create();

        answers.add(defendantAnswerField);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers);

        Assertions.assertEquals(0, actual.getValidationResults().size());

    }

    @Test
    @DisplayName("Error: no document type found ")
    public void withInValidDocumentTypeThrowException() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(NOT_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(THE_VALUE))
                .create();

        answers.add(answerField);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers);

        Assertions.assertEquals(ValidationTypes.DOCUMENT_TYPE, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals("No Document Found", actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("Response to Civil Claim", actual.getValidationResults().get(0).getExpected());

    }

    @Test
    @DisplayName("Error: invalid document type found")
    public void withInValidDocumentThrowException() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField answerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(NOT_RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(answerField);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers);

        Assertions.assertEquals(ValidationTypes.DOCUMENT_TYPE, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals(NOT_RESPONSE_TO_CIVIL_CLAIM, actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("Response to Civil Claim", actual.getValidationResults().get(0).getExpected());

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

        Assertions.assertThrows(AiReviewerRestrictedDocumentException.class, () -> sut.validateExtractedDocument(BigDecimal.ONE, DOCUMENT_TYPE_CONFIGURATION, answers));

    }

    @Test
    @DisplayName("Error: too many plaintiffs found")
    public void withInvalidPartiesPlaintiffs() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField documentAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(documentAnswerField);

        DiligenAnswerField plaintiffAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_PLAINTIFF_ID)
                .values(Arrays.asList(PLAINTIFF, PLAINTIFF))
                .create();

        answers.add(plaintiffAnswerField);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers);

        Assertions.assertEquals(ValidationTypes.PARTIES_PLAINTIFF, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals("2", actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("1", actual.getValidationResults().get(0).getExpected());

    }

    @Test
    @DisplayName("Error: too many defendants found")
    public void withInvalidPartiesDefendants() {

        List<DiligenAnswerField> answers= new ArrayList<>();
        DiligenAnswerField documentAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DOCUMENT_TYPE_ID)
                .values(Collections.singletonList(RESPONSE_TO_CIVIL_CLAIM))
                .create();

        answers.add(documentAnswerField);

        DiligenAnswerField defendantAnswerField = DiligenAnswerField.builder()
                .id(Keys.ANSWER_DEFENDANT_ID)
                .values(Arrays.asList(DEFENDANT, DEFENDANT))
                .create();

        answers.add(defendantAnswerField);

        DocumentValidation actual = sut.validateExtractedDocument(BigDecimal.ZERO, DOCUMENT_TYPE_CONFIGURATION, answers);

        Assertions.assertEquals(ValidationTypes.PARTIES_DEFENDANT, actual.getValidationResults().get(0).getType());
        Assertions.assertEquals("2", actual.getValidationResults().get(0).getActual());
        Assertions.assertEquals("1", actual.getValidationResults().get(0).getExpected());

    }

}
