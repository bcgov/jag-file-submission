package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtBase;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Party;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenerateUrlRequestValidatorImplTest {

    private static final String COURT_CLASSIFICATION = "COURT_CLASSIFICATION";
    private static final String[] ROLE_TYPES = new String[] { "ADJ", "CIT" };
    private static final String COURT_LEVEL = "COURT_LEVEL";

    private GenerateUrlRequestValidatorImpl sut;

    @Mock
    private SubmissionService submissionService;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(submissionService
                .getValidPartyRoles(
                        ArgumentMatchers.argThat(x -> x.getCourtClassification().equals(COURT_CLASSIFICATION))))
                .thenReturn(Arrays.asList(ROLE_TYPES));

        sut = new GenerateUrlRequestValidatorImpl(submissionService);

    }

    @Test
    @DisplayName("ok: without error should return a notification without error")
    public void withoutErrorShouldReturnNoError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
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
        Notification actual = sut.validate(generateUrlRequest);

        Assertions.assertFalse(actual.hasError());

    }

    @Test
    @DisplayName("Error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Notification actual = sut.validate(generateUrlRequest);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Initial Package is required.", actual.getErrors().get(0));

    }


    @Test
    @DisplayName("Error: without filing package should return an error")
    public void withoutFilingPackageShouldReturnError2() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("At least 1 party is required for new submission.", actual.getErrors().get(0));

    }

    @Test
    @DisplayName("error: with role type not in list should return multiple errors")
    public void withRoleTypeNotInListShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
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
        Notification actual = sut.validate(generateUrlRequest);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Role type [CAV] invalid.", actual.getErrors().get(0));
        Assertions.assertEquals("Role type [DEO] invalid.", actual.getErrors().get(1));

    }

    @Test
    @DisplayName("error: with role type Null should return error")
    public void withRoleTypeNullNotInListShouldReturnMultipleErrors() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPackage = new InitialPackage();

        CourtBase court = new CourtBase();
        court.setLevel(COURT_LEVEL);
        court.setCourtClass(COURT_CLASSIFICATION);
        initialFilingPackage.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        parties.add(party);
        initialFilingPackage.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPackage);
        Notification actual = sut.validate(generateUrlRequest);

        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("Role type [null] invalid.", actual.getErrors().get(0));

    }

}
