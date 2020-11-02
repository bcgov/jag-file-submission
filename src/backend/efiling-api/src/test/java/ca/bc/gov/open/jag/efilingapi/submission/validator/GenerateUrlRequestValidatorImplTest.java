package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtBase;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Party;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenerateUrlRequestValidatorImplTest {

    private GenerateUrlRequestValidatorImpl sut;


    @BeforeEach
    public void setup() {
        sut = new GenerateUrlRequestValidatorImpl();
    }

    @Test
    @DisplayName("ok: without error should return a notification without error")
    public void withoutErrorShouldReturnNoError() {

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
        InitialPackage initialFilingPacakge = new InitialPackage();

        CourtBase court = new CourtBase();
        initialFilingPacakge.setCourt(court);

        List<Party> parties = new ArrayList<>();
        Party party = new Party();
        parties.add(party);
        initialFilingPacakge.setParties(parties);

        generateUrlRequest.setFilingPackage(initialFilingPacakge);
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
        InitialPackage initialFilingPacakge = new InitialPackage();

        CourtBase court = new CourtBase();
        initialFilingPacakge.setCourt(court);

        generateUrlRequest.setFilingPackage(initialFilingPacakge);
        Notification actual = sut.validate(generateUrlRequest);


        Assertions.assertTrue(actual.hasError());
        Assertions.assertEquals("At least 1 party is required for new submission.", actual.getErrors().get(0));

    }

}
