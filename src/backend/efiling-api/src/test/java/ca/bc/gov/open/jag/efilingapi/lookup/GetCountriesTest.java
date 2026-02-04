package ca.bc.gov.open.jag.efilingapi.lookup;

import ca.bc.gov.open.jag.efilingapi.api.model.CountryCode;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetCountriesTest {

    private static final String TEST = "TEST";
    private static final String TEST_1 = "TEST_1";
    private static final String TEST_2 = "TEST_2";

    CountriesApiDelegateImpl sut;

    @Mock
    EfilingLookupService efilingLookupServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(efilingLookupServiceMock.getCountries()).thenReturn(createLookupList());

        sut = new CountriesApiDelegateImpl(efilingLookupServiceMock);

    }

    @Test
    @DisplayName("200: File is returned")
    public void withValidRequestReturnFilingPackage() {

        ResponseEntity<List<CountryCode>> result = sut.getCountries();

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(2, result.getBody().size());

        Assertions.assertEquals(TEST_1, result.getBody().get(0).getCode());
        Assertions.assertEquals(TEST, result.getBody().get(0).getDescription());

        Assertions.assertEquals(TEST_2, result.getBody().get(1).getCode());
        Assertions.assertEquals(TEST, result.getBody().get(1).getDescription());

    }

    private List<LookupItem> createLookupList() {

        return Arrays.asList(
                LookupItem.builder().code(TEST_1).description(TEST).create(),
                LookupItem.builder().code(TEST_2).description(TEST).create());

    }

}
