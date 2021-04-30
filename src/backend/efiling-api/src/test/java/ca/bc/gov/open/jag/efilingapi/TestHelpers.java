package ca.bc.gov.open.jag.efilingapi;

import ca.bc.gov.open.jag.efilingapi.api.model.InitialDocument;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.NavigationUrls;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.*;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.*;

public class TestHelpers {

    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    public static final UUID CASE_2 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID CASE_3 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID CASE_4 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
    public static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fd");

    public static final String CASE_1_STRING = CASE_1.toString();
    public static final String CASE_2_STRING = CASE_2.toString();

    public static final String IDENTITY_PROVIDER = "IDENTITY_PROVIDER";
    public static final String DIVISION = "DIVISION";
    public static final String FILENUMBER = "FILENUMBER";
    public static final String LEVEL = "LEVEL";
    public static final String LOCATION = "1211";
    public static final String PARTICIPATIONCLASS = "PARTICIPATIONCLASS";
    public static final String PROPERTYCLASS = "PROPERTYCLASS";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String COURT_DESCRIPTION = "TESTCOURTDESC";
    public static final String LEVEL_DESCRIPTION = "TESTLEVELDESC";
    public static final String CLASS_DESCRIPTION = "TESTCLASSDESC";

    public static final String SUCCESS_URL = "http://success";
    public static final String CANCEL_URL = "http://cancel";
    public static final String ERROR_URL = "http://error";

    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String MIDDLE_NAME = "MIDDLENAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String NAME_TYPE_CD = "NAMECD";
    public static final String ROLE_TYPE_CD = "ABC";

    public static final String COURT_CLASS = "COURT_CLASS";
    public static final String FILE_NUMBER = "FILE_NUMBER";
    public static final String LOCATION_DESCRIPTION = "LOCATION_DESCRIPTION";
    public static final String PARTICIPATING_CLASS = "PARTICIPATING_CLASS";
    public static final String NAME_TYPE = "NAME_TYPE";
    public static final String NAME = "NAME";
    public static final String STATUS = "STATUS";
    public static final String STATUS_CODE = "STATUSCODE";
    public static final String COMMENT = "COMMENT";
    public static final String PACKAGE_NO = "123";
    public static final String DOCUMENT_ID_ONE = "1";
    public static final String DOCUMENT_ID_TWO = "2";
    public static final BigDecimal DOCUMENT_ID_TWO_BD = new BigDecimal(2);

    public static final String TYPE = "AAB";
    public static final String PARTY_TYPE_DESC = "PARTY_TYPE_DESC";
    public static final String ROLE_TYPE_DESC = "ROLE_TYPE_DESC";
    public static final String TRANSACTION_DESC = "TRANSACTION_DESC";


    public static InitialPackage createInitalPackage(ca.bc.gov.open.jag.efilingapi.api.model.CourtBase court, List<InitialDocument> initialDocuments) {
        InitialPackage initialPackage = new InitialPackage();
        initialPackage.setCourt(court);
        initialPackage.setDocuments(initialDocuments);
        initialPackage.setParties(new ArrayList<>());
        initialPackage.setOrganizationParties(new ArrayList<>());
        return initialPackage;
    }

    public static NavigationUrls createNavigation(String success, String cancel, String error) {
        NavigationUrls navigation = new NavigationUrls();
        navigation.setSuccess(success);
        navigation.setCancel(cancel);
        navigation.setError(error);
        return navigation;
    }

    public static ca.bc.gov.open.jag.efilingapi.api.model.Court createApiCourt() {
        ca.bc.gov.open.jag.efilingapi.api.model.Court court = new ca.bc.gov.open.jag.efilingapi.api.model.Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setCourtClass(PROPERTYCLASS);
        court.setAgencyId(BigDecimal.TEN);
        court.setLevelDescription(LEVEL_DESCRIPTION);
        court.setClassDescription(CLASS_DESCRIPTION);
        court.setLocationDescription(COURT_DESCRIPTION);
        return court;
    }

    public static ca.bc.gov.open.jag.efilingapi.api.model.Court createApiCourt(String location) {
        ca.bc.gov.open.jag.efilingapi.api.model.Court court = new ca.bc.gov.open.jag.efilingapi.api.model.Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(location);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setCourtClass(PROPERTYCLASS);
        court.setAgencyId(BigDecimal.TEN);
        court.setLevelDescription(LEVEL_DESCRIPTION);
        court.setClassDescription(CLASS_DESCRIPTION);
        court.setLocationDescription(COURT_DESCRIPTION);
        return court;
    }

    public static ca.bc.gov.open.jag.efilingapi.api.model.CourtBase createApiCourtBase(String location) {
        ca.bc.gov.open.jag.efilingapi.api.model.CourtBase court = new ca.bc.gov.open.jag.efilingapi.api.model.CourtBase();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(location);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setCourtClass(PROPERTYCLASS);
        return court;
    }


    public static Court createCourt() {
        return Court.builder()
                .division(DIVISION)
                .fileNumber(FILENUMBER)
                .level(LEVEL)
                .location(LOCATION)
                .participatingClass(PARTICIPATIONCLASS)
                .courtClass(PROPERTYCLASS)
                .agencyId(BigDecimal.TEN)
                .levelDescription(LEVEL_DESCRIPTION)
                .classDescription(CLASS_DESCRIPTION)
                .locationDescription(COURT_DESCRIPTION).create();
    }

    public static Submission buildSubmission() {
        return Submission
                .builder()
                .filingPackage(createPackage(createCourt(), createDocumentList(), createPartyList(), createOrganizationList()))
                .navigationUrls(createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL))
                .create();
    }

    public static FilingPackage createPackage(
            Court court,
            List<Document> documents,
            List<Individual> parties,
            List<Organization> organizations) {
        return FilingPackage.builder()
                .court(court)
                .documents(documents)
                .parties(parties)
                .organizations(organizations)
                .create();
    }

    public static List<InitialDocument> createInitialDocumentsList() {
        InitialDocument initialDocument = new InitialDocument();
        initialDocument.setName("random.txt");
        initialDocument.setType("AAB");
        return Arrays.asList(initialDocument);
    }

    public static List<DocumentTypeDetails> createValidDocumentTypesList() {
        DocumentTypeDetails documentTypeDetails = new DocumentTypeDetails(DESCRIPTION, TYPE, BigDecimal.TEN, false, false, false);
        return Arrays.asList(documentTypeDetails);
    }

    public static List<DocumentTypeDetails> createInvalidDocumentTypesList() {
        DocumentTypeDetails documentTypeDetails = new DocumentTypeDetails(DESCRIPTION, "ZZZ", BigDecimal.TEN, false, false, false);
        return Arrays.asList(documentTypeDetails);
    }

    public static List<String> createInvalidPartyRoles() {
        List<String> invalidRoles = new ArrayList<>();
        invalidRoles.add(ROLE_TYPE_CD);

        return invalidRoles;
    }

    public static List<String> createValidPartyRoles() {
        List<String> validRoles = new ArrayList<>();
        validRoles.add("ABC");

        return validRoles;
    }

    public static List<Document> createDocumentList() {
        return Arrays.asList(Document.builder()
                .description(DESCRIPTION)
                .statutoryFeeAmount(BigDecimal.TEN)
                .name("random.txt")
                .type(TYPE)
                .subType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD)
                .mimeType("application/txt")
                .isSupremeCourtScheduling(true)
                .isAmendment(true)
                .data(new Object()).create());
    }

    public static List<Individual> createPartyList() {

        Individual individualOne = buildParty();

        Individual individualTwo = buildParty();
        return Arrays.asList(individualOne, individualTwo);
    }

    public static List<Organization> createOrganizationList() {

        Organization orgOne = buildOrganization();

        Organization orgTwo = buildOrganization();
        return Arrays.asList(orgOne, orgTwo);
    }

    private static Individual buildParty() {
        return Individual.builder()
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .nameTypeCd(NAME_TYPE_CD)
                .roleTypeCd(ROLE_TYPE_CD)
                .create();
    }

    private static Organization buildOrganization() {
        return Organization.builder()
                .name(NAME)
                .nameTypeCd(NAME_TYPE_CD)
                .roleTypeCd(ROLE_TYPE_CD)
                .create();
    }

    public static AccountDetails createCSOAccountDetails(boolean isFileRolePresent) {
        return AccountDetails.builder().fileRolePresent(isFileRolePresent).create();
    }

    public static NavigationUrls createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }

    public static AccountDetails createAccount(BigDecimal clientId) {

        return AccountDetails.builder()
                .fileRolePresent(true)
                .accountId(BigDecimal.ONE)
                .clientId(clientId)
                .cardRegistered(true)
                .universalId(UUID.randomUUID().toString())
                .internalClientNumber(null)
                .universalId(TestHelpers.CASE_1_STRING).create();

    }

    public static ReviewFilingPackage createFilingPackage() {

        ReviewFilingPackage reviewFilingPackage = new ReviewFilingPackage();
        reviewFilingPackage.setClientFileNo("CLIENTFILENO");
        reviewFilingPackage.setFilingCommentsTxt(COMMENT);
        reviewFilingPackage.setPackageNo(PACKAGE_NO);
        reviewFilingPackage.setFirstName(FIRST_NAME);
        reviewFilingPackage.setLastName(LAST_NAME);
        reviewFilingPackage.setSubmittedDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewFilingPackage.setCourt(createReviewCourt());
        reviewFilingPackage.setDocuments(createDocuments());
        reviewFilingPackage.setParties(Collections.singletonList(createParty()));
        reviewFilingPackage.setPayments(Collections.singletonList(createPayment()));
        reviewFilingPackage.setPackageLinks(PackageLinks.builder().packageHistoryUrl("http://localhost:8080/showmustgoon").create());
        return reviewFilingPackage;

    }

    public static ReviewCourt createReviewCourt() {

        ReviewCourt reviewCourt = new ReviewCourt();
        reviewCourt.setClassDescription(CLASS_DESCRIPTION);
        reviewCourt.setCourtClass(COURT_CLASS);
        reviewCourt.setDivision(DIVISION);
        reviewCourt.setFileNumber(FILE_NUMBER);
        reviewCourt.setLevel(LEVEL);
        reviewCourt.setLevelDescription(LEVEL_DESCRIPTION);
        reviewCourt.setLocationId(BigDecimal.ONE);
        reviewCourt.setLocationName(LOCATION);
        reviewCourt.setLocationDescription(LOCATION_DESCRIPTION);
        reviewCourt.setParticipatingClass(PARTICIPATING_CLASS);
        return reviewCourt;

    }

    public static Individual createParty() {
        return Individual.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .middleName(MIDDLE_NAME)
                .nameTypeCd(NAME_TYPE)
                .partyTypeDesc(PARTY_TYPE_DESC)
                .roleTypeCd(ca.bc.gov.open.jag.efilingapi.api.model.Party.RoleTypeEnum.ABC.getValue())
                .roleTypeDesc(ROLE_TYPE_DESC)
                .create();
    }

    public static List<ReviewDocument> createDocuments() {

        ReviewDocument reviewDocumentOne = new ReviewDocument();
        reviewDocumentOne.setDocumentId(DOCUMENT_ID_ONE);
        reviewDocumentOne.setFileName(NAME);
        reviewDocumentOne.setDocumentTypeCd("AAB");
        reviewDocumentOne.setDocumentType(DESCRIPTION);
        reviewDocumentOne.setStatus(STATUS);
        reviewDocumentOne.setStatusCode(STATUS_CODE);
        reviewDocumentOne.setDateFiled(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewDocumentOne.setStatusDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewDocumentOne.setPaymentProcessed(true);

        ReviewDocument reviewDocumentTwo = new ReviewDocument();
        reviewDocumentTwo.setDocumentId(DOCUMENT_ID_TWO);
        reviewDocumentTwo.setFileName(NAME);
        reviewDocumentTwo.setDocumentTypeCd("AAB");
        reviewDocumentTwo.setDocumentType(DESCRIPTION);
        reviewDocumentTwo.setStatus(STATUS);
        reviewDocumentTwo.setStatusCode(STATUS_CODE);
        reviewDocumentTwo.setDateFiled(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewDocumentTwo.setStatusDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewDocumentTwo.setPaymentProcessed(true);

        ArrayList<ReviewDocument> documents = new ArrayList<>();
        documents.add(reviewDocumentOne);
        documents.add(reviewDocumentTwo);
        return documents;

    }

    public static PackagePayment createPayment() {

        PackagePayment packagePayment = new PackagePayment();
        packagePayment.setFeeExmpt(false);
        packagePayment.setPaymentCategory(BigDecimal.ONE);
        packagePayment.setProcessedAmt(BigDecimal.ONE);
        packagePayment.setSubmittedAmt(BigDecimal.ONE);
        packagePayment.setServiceId(BigDecimal.ONE);
        packagePayment.setTransactionDesc(TRANSACTION_DESC);
        packagePayment.setTransactionDtm(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        return packagePayment;

    }

}
