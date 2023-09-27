package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetPaymentReceiptTest")
public class GetPaymentReceiptTest {
    public static final UUID CASE_1 = UUID.randomUUID();
    public static final UUID CASE_2 = UUID.randomUUID();

    public static final byte[] BYTES = "TEST".getBytes();

    FilingpackageApiDelegateImpl sut;

    @Mock
    FilingPackageService filingPackageService;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private Jwt jwtMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        sut = new FilingpackageApiDelegateImpl(filingPackageService);

    }

    @Test
    @DisplayName("200: ok url was generated")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(filingPackageService.getReport(Mockito.any())).thenReturn(Optional.of(new ByteArrayResource(BYTES)));

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<Resource> result = sut.getPaymentReceipt(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should return 403")
    public void withNoUniversalIdShouldReturn403() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<?> actual = sut.getPaymentReceipt(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: when no filling package is found return 404")
    public void withValidRequestFilingPackageNotFound() {

        Mockito.when(filingPackageService.getReport(Mockito.any())).thenReturn(Optional.empty());

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<Resource> result = sut.getPaymentReceipt(BigDecimal.TEN);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

}
