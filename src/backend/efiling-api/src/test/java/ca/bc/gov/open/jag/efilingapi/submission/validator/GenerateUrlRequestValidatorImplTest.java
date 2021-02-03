package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenerateUrlRequestValidatorImplTest {

    private static final String COURT_CLASSIFICATION = "COURT_CLASSIFICATION";
    private static final String[] ROLE_TYPES = new String[] { "ADJ", "CIT" };
    private static final String COURT_LEVEL = "COURT_LEVEL";
    private static final String APPLICATION_CODE = "app_code";
    private static final String COURT_DESCRIPTION = "courtDescription";
    private static final String CLASS_DESCRIPTION = "classDescription";
    private static final String LEVEL_DESCRIPTION = "levelDescription";
    private static final BigDecimal COURT_ID = BigDecimal.ONE;
    private static final String CASE_1 = "CASE1";
    private static final String CASE_2 = "case2";
    private static final BigDecimal COURT_ID_2 = BigDecimal.TEN;
    private static final String COURT_LOCATION = "court location";
    public static final String FILE_NUMBER_SUCCESS = "filenumber";
    public static final String FILE_NUMBER_ERROR = "file_number_error";

    private GenerateUrlRequestValidatorImpl sut;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private CourtService courtServiceMock;

    @Mock
    private DocumentService documentServiceMock;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(submissionService
                .getValidPartyRoles(
                        ArgumentMatchers.argThat(x -> x.getCourtClassification().equals(COURT_CLASSIFICATION))))
                .thenReturn(Arrays.asList(ROLE_TYPES));

        CourtDetails courtDetails = new CourtDetails(COURT_ID, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);

        Mockito
                .doReturn(courtDetails)
                .when(courtServiceMock).getCourtDetails(ArgumentMatchers.argThat(x -> x.getCourtLocation().equals(CASE_1)));

        Mockito
                .doReturn(true)
                .when(courtServiceMock).isValidCourt(ArgumentMatchers.argThat(x -> x.getCourtId().equals(COURT_ID)));

        CourtDetails courtDetails2 = new CourtDetails(COURT_ID_2, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);

        Mockito
                .doReturn(courtDetails2)
                .when(courtServiceMock)
                .getCourtDetails(ArgumentMatchers.argThat(x -> x.getCourtLocation().equals(CASE_2)));

        Mockito
                .doReturn(false)
                .when(courtServiceMock)
                .isValidCourt(ArgumentMatchers.argThat(x -> x.getCourtId().equals(COURT_ID_2)));


        Mockito
                .doReturn(true)
                .when(courtServiceMock)
                .isValidCourtFileNumber(ArgumentMatchers.argThat(x -> x.getFileNumber().equals(FILE_NUMBER_SUCCESS)));

        Mockito
                .doReturn(false)
                .when(courtServiceMock)
                .isValidCourtFileNumber(ArgumentMatchers.argThat(x -> x.getFileNumber().equals(FILE_NUMBER_ERROR)));

        List<DocumentType> documentTypes = new ArrayList<>();
        DocumentType document = new DocumentType("Description", "ACMW", true);
        documentTypes.add(document);

        Mockito
                .doReturn(documentTypes)
                .when(documentServiceMock)
                .getValidDocumentTypes(ArgumentMatchers.argThat(x -> x.getCourtLevel().equals(COURT_LEVEL)));



        sut = new GenerateUrlRequestValidatorImpl(submissionService, courtServiceMock, documentServiceMock);

    }

    @Test
    @DisplayName("ok: new submission without error should return a notification without error")
    public void newSubmissionWithoutErrorShouldReturnNoError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.ADJ);
        parties.add(party);
        Party party2 = new Party();
        party2.setRoleType(Party.RoleTypeEnum.CIT);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(DocumentProperties.TypeEnum.ACMW);
        initialDocument.setDocumentProperties(documentProperties);
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("ok: returning submission without error should return a notification without error")
    public void returningSubmissionwithoutErrorShouldReturnNoError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);


        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(DocumentProperties.TypeEnum.ACMW);
        initialDocument.setDocumentProperties(documentProperties);
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);



        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("error: with no courtDetails should return error")
    public void withNoCourtDetailsShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation("unknown");
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        parties.add(party);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Court with Location: [unknown], Level: [COURT_LEVEL], Classification: [COURT_CLASSIFICATION] is not a valid court.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("ok: returning submission with invalid filenumber should return a notification with error")
    public void returningSubmissionWithInvalidFileNumberShouldReturnNotificationWithErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        court.setFileNumber(FILE_NUMBER_ERROR);
        initialFilingPackage.setCourt(court);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("FileNumber [file_number_error] does not exists.", actual.getErrors().get(0));
    }


    @Test
    @DisplayName("error: with invalid court should return an error")
    public void withInvalidCourtShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_2);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.ADJ);
        parties.add(party);
        Party party2 = new Party();
        party2.setRoleType(Party.RoleTypeEnum.CIT);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Court with Location: [case2], Level: [COURT_LEVEL], Classification: [COURT_CLASSIFICATION] is not a valid court.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("Error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Initial Package is required.", actual.getErrors().get(0));

    }


    @Test
    @DisplayName("Error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError2() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("At least 1 party is required for new submission.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with role type not in list should return multiple errors")
    public void withRoleTypeNotInListShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.CAV);
        parties.add(party);
        Party party2 = new Party();
        party2.setRoleType(Party.RoleTypeEnum.DEO);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Role type [CAV] is invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Role type [DEO] is invalid.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: with role type not in list and with fileNumber should return multiple errors")
    public void withRoleTypeNotInListAnFileNumberSetShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.CAV);
        parties.add(party);
        Party party2 = new Party();
        party2.setRoleType(Party.RoleTypeEnum.DEO);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Role type [CAV] is invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Role type [DEO] is invalid.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: with role type Null should return error")
    public void withRoleTypeNullNotInListShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        parties.add(party);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Role type [null] is invalid.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with invalid document type should return notification with Error")
    public void withInvalidDocumentTypeShouldReturnNotificationWithError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        party.setRoleType(Party.RoleTypeEnum.ADJ);
        parties.add(party);
        Party party2 = new Party();
        party2.setRoleType(Party.RoleTypeEnum.CIT);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(DocumentProperties.TypeEnum.TAX);
        initialDocument.setDocumentProperties(documentProperties);
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Document type [TAX] is invalid.", actual.getErrors().get(0));

    }



}
