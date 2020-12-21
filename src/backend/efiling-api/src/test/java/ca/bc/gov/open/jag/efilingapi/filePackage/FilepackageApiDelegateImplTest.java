package ca.bc.gov.open.jag.efilingapi.filePackage;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PaymentApiDelegateImpl")
public class FilepackageApiDelegateImplTest {
    FilepackageApiDelegateImpl sut;

    @BeforeAll
    public void beforeAll() {
        sut = new FilepackageApiDelegateImpl();
    }

    @Test
    @DisplayName("200: ok url was generated")
    public void withValidRequestReturnFilingPackage() {
        ResponseEntity<FilingPackage> result = sut.getFilePackage(BigDecimal.ONE, BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }
}
