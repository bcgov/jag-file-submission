package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class FilingPackageServiceImplTest {

    FilingPackageServiceImpl sut;

    @Mock
    EfilingStatusService efilingStatusServiceMock;

    @Mock
    AccountService accountServiceMock;

    @BeforeAll
    public void beforeAll() {
        sut = new FilingPackageServiceImpl(efilingStatusServiceMock, accountServiceMock);
    }

    @Test
    @DisplayName("Ok: a filing package was returned")
    public void withValidRequestReturnFilingPackage() {
        Optional<FilingPackage> result = sut.getCSOFilingPackage(UUID.randomUUID(), BigDecimal.ONE);
        //This test for now will return empty optional
        Assertions.assertTrue(!result.isPresent());
    }
}
