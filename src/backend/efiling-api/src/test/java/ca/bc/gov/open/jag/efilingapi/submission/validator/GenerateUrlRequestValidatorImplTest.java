package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
    private static final String FILE_NUMBER_SUCCESS = "filenumber";
    private static final String FILE_NUMBER_ERROR = "file_number_error";
    private static final String ORGANIZATION_NAME = "ORGANIZATION NAME";
    private static final String LAST_NAME = "LAST NAME";
    private static final BigDecimal EXISTING_PACKAGE = BigDecimal.ONE;
    private static final BigDecimal EXISTING_DOCUMENT = BigDecimal.ONE;

    private GenerateUrlRequestValidatorImpl sut;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private CourtService courtServiceMock;

    @Mock
    private DocumentService documentServiceMock;

    @Mock
    private FilingPackageService filingPackageServiceMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(submissionService
                .getValidPartyRoles(
                        ArgumentMatchers.argThat(x -> x.getCourtClassification().equals(COURT_CLASSIFICATION))))
                .thenReturn(Arrays.asList(ROLE_TYPES));

        CourtDetails courtDetails = new CourtDetails(COURT_ID, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);

        Mockito
                .doReturn(Optional.of(courtDetails))
                .when(courtServiceMock)
                .getCourtDetails(ArgumentMatchers.argThat(x -> x.getCourtLocation().equals(CASE_1)));

        Mockito
                .doReturn(true)
                .when(courtServiceMock).isValidCourt(ArgumentMatchers.argThat(x -> x.getCourtId().equals(COURT_ID)));

        CourtDetails courtDetails2 = new CourtDetails(COURT_ID_2, COURT_DESCRIPTION, CLASS_DESCRIPTION, LEVEL_DESCRIPTION);

        Mockito
                .doReturn(Optional.of(courtDetails2))
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

        List<DocumentTypeDetails> documentTypeDetails = new ArrayList<>();
        DocumentTypeDetails document = new DocumentTypeDetails("Description", "ACMW", BigDecimal.TEN,true, true, true);
        documentTypeDetails.add(document);

        Mockito
                .doReturn(documentTypeDetails)
                .when(documentServiceMock)
                .getValidDocumentTypes(ArgumentMatchers.argThat(x -> x.getCourtLevel().equals(COURT_LEVEL)));

        Optional<FilingPackage> optionalFilingPackage = Optional.of(setupPackage(EXISTING_PACKAGE, EXISTING_DOCUMENT));

        Mockito.when(filingPackageServiceMock.getCSOFilingPackage(any(), ArgumentMatchers.eq(EXISTING_PACKAGE))).thenReturn(optionalFilingPackage);

        sut = new GenerateUrlRequestValidatorImpl(submissionService, courtServiceMock, documentServiceMock, filingPackageServiceMock);

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        party2.setLastName(LAST_NAME);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("ACMW");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("ok: returning submission without error should return a notification without error")
    public void returningSubmissionWithoutErrorShouldReturnNoError() {

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
        initialDocument.setType("ACMW");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.APP);
        party.setLastName(LAST_NAME);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("ok: returning submission with existing should return a notification without error")
    public void returningSubmissionWithExistingPackageErrorShouldReturnNoError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        initialFilingPackage.setPackageNumber(EXISTING_PACKAGE);

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("ACMW");
        ActionDocument actionDocument = new ActionDocument();
        actionDocument.setId(EXISTING_DOCUMENT);
        initialDocument.setActionDocument(actionDocument);
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.APP);
        party.setLastName(LAST_NAME);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("error: with no navigation urls should return error")
    public void newSubmissionWithoutNavigationUrlsShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        initialFilingPackage.setCourt(court);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        party2.setLastName(LAST_NAME);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("ACMW");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Navigation Urls are required.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with blank navigation urls should return error")
    public void newSubmissionWithBlankNavigationUrlsShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        initialFilingPackage.setCourt(court);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        party2.setLastName(LAST_NAME);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("ACMW");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        generateUrlRequest.setFilingPackage(initialFilingPackage);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError(" ");
        navigationUrls.setCancel("         ");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");


        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Error url is required.", actual.getErrors().get(0));
        Assertions.assertEquals("Cancel url is required.", actual.getErrors().get(1));
        Assertions.assertEquals("Success url is required.", actual.getErrors().get(2));

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setLastName(LAST_NAME);
        parties.add(party);
        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

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

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.APP);
        party.setLastName(LAST_NAME);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        party2.setLastName(LAST_NAME);
        parties.add(party2);

        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Court with Location: [case2], Level: [COURT_LEVEL], Classification: [COURT_CLASSIFICATION] is not a valid court.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Initial Package is required.", actual.getErrors().get(0));

    }


    @Test
    @DisplayName("error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError2() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.CAV);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.DEO);
        party2.setLastName(LAST_NAME);
        parties.add(party2);

        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Individual role type [CAV] is invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Individual role type [DEO] is invalid.", actual.getErrors().get(1));

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.CAV);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.DEO);
        party2.setLastName(LAST_NAME);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Individual role type [CAV] is invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Individual role type [DEO] is invalid.", actual.getErrors().get(1));

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setLastName(LAST_NAME);
        parties.add(party);
        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Individual role type [null] is invalid.", actual.getErrors().get(0));

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

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        party.setLastName(LAST_NAME);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        party2.setLastName(LAST_NAME);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("TAX");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Document type [TAX] is invalid.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with null parties should return notification with Error")
    public void withNullPartiesShouldReturnNotificationWithError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        initialFilingPackage.setCourt(court);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("TAX");
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("At least 1 party is required for new submission.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with role type not in list and with fileNumber should return multiple errors")
    public void withOrganizeationRoleTypeNotInListAnFileNumberSetShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<Organization> orgs = new ArrayList<>();
        Organization org1 = new Organization();
        org1.setRoleType(Organization.RoleTypeEnum.CAV);
        org1.setName(ORGANIZATION_NAME);
        orgs.add(org1);
        Organization org2 = new Organization();
        org2.setRoleType(Organization.RoleTypeEnum.DEO);
        org2.setName(ORGANIZATION_NAME);
        orgs.add(org2);
        initialFilingPackage.setOrganizationParties(orgs);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Organization role type [CAV] is invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Organization role type [DEO] is invalid.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: with null org name and with fileNumber should return multiple errors")
    public void withNullOrganizeationNameInListAnFileNumberSetShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<Organization> orgs = new ArrayList<>();
        Organization org1 = new Organization();
        org1.setRoleType(Organization.RoleTypeEnum.ADJ);
        orgs.add(org1);
        Organization org2 = new Organization();
        org2.setRoleType(Organization.RoleTypeEnum.CIT);
        orgs.add(org2);
        initialFilingPackage.setOrganizationParties(orgs);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Organization name is required.", actual.getErrors().get(0));
        Assertions.assertEquals("Organization name is required.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: with null last name and with fileNumber should return multiple errors")
    public void withNullIndividualLastNameInListAnFileNumberSetShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLocation(CASE_1);
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.ADJ);
        parties.add(party);
        Individual party2 = new Individual();
        party2.setRoleType(Individual.RoleTypeEnum.CIT);
        parties.add(party2);
        initialFilingPackage.setParties(parties);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Individual last name is required.", actual.getErrors().get(0));
        Assertions.assertEquals("Individual last name is required.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: returning submission with bad package number should return a error")
    public void returningSubmissionWithBadPackageErrorShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        initialFilingPackage.setPackageNumber(BigDecimal.TEN);

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        List<InitialDocument> documentList = new ArrayList<>();
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setType("ACMW");
        ActionDocument actionDocument = new ActionDocument();
        actionDocument.setId(EXISTING_DOCUMENT);
        initialDocument.setActionDocument(actionDocument);
        documentList.add(initialDocument);
        initialFilingPackage.setDocuments(documentList);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.APP);
        party.setLastName(LAST_NAME);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());

        Assertions.assertEquals("For given package number and universal id no record was found in cso", actual.getErrors().get(0));

    }


    @Test
    @DisplayName("error: returning submission with no documents should return a error")
    public void returningSubmissionWithNoDocumentShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        initialFilingPackage.setPackageNumber(EXISTING_PACKAGE);

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        court.setLocation(CASE_1);
        court.setFileNumber(FILE_NUMBER_SUCCESS);
        initialFilingPackage.setCourt(court);

        NavigationUrls navigationUrls = new NavigationUrls();
        navigationUrls.setError("http://error");
        navigationUrls.setCancel("http://cancel");
        navigationUrls.setSuccess("http://success");
        generateUrlRequest.setNavigationUrls(navigationUrls);

        List<Individual> parties = new ArrayList<>();
        Individual party = new Individual();
        party.setRoleType(Individual.RoleTypeEnum.APP);
        party.setLastName(LAST_NAME);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest, APPLICATION_CODE, "");

        Assertions.assertTrue(actual.hasError());

        Assertions.assertEquals("For given package there are no documents present", actual.getErrors().get(0));

    }

    private FilingPackage setupPackage(BigDecimal packageNumber, BigDecimal documentNumber) {

        FilingPackage filingPackage = new FilingPackage();

        filingPackage.setPackageNumber(packageNumber);

        Document document = new Document();

        document.setIdentifier(documentNumber.toPlainString());

        filingPackage.getDocuments().add(document);

        return filingPackage;

    }


}
