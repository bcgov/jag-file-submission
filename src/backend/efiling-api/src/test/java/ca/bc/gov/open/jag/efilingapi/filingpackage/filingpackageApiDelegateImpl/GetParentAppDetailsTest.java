package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentAppDetails;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.FilingPackageNotFoundException;
import ca.bc.gov.open.jag.efilingapi.error.MissingUniversalIdException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilepackageApiDelegateImplTest")
public class GetParentAppDetailsTest {

    public static final UUID CASE_1 = UUID.randomUUID();
    public static final UUID CASE_2 = UUID.randomUUID();

    private static final String RETURN_URL = "http://localhost:8080/";

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
    public void withValidRequestReturnParentAppDetails() {

        ParentAppDetails parentAppDetails = new ParentAppDetails();

        parentAppDetails.setReturnUrl(RETURN_URL);
        parentAppDetails.setRejectedDocumentFeature(true);

        Mockito.when(filingPackageService.getParentDetails(Mockito.any(), Mockito.any())).thenReturn(Optional.of(parentAppDetails));

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<ParentAppDetails> result = sut.getParentDetails(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(RETURN_URL, result.getBody().getReturnUrl());
        Assertions.assertTrue(result.getBody().getRejectedDocumentFeature());

    }

    @Test
    @DisplayName("403: when no universal id should return 403")
    public void withNoUniversalIdShouldReturn403() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getParentDetails(BigDecimal.ONE));
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());

    }

    @Test
    @DisplayName("404: when no filling package is found return 404")
    public void withValidRequestFilingPackageNotFound() {

        Mockito.when(filingPackageService.getParentDetails(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getParentDetails(BigDecimal.TEN));
        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());

    }

}
