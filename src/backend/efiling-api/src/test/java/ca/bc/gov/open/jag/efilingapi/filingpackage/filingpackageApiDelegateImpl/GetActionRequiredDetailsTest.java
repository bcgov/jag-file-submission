package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.ActionRequiredDetails;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.FilingPackageNotFoundException;
import ca.bc.gov.open.jag.efilingapi.error.MissingUniversalIdException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilepackageApiDelegateImplTest")
public class GetActionRequiredDetailsTest {

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

        Mockito.when(filingPackageService.getActionRequiredDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING), ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(Optional.of(new ActionRequiredDetails()));


        Mockito.when(filingPackageService.getActionRequiredDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING), ArgumentMatchers.eq(BigDecimal.TEN))).thenReturn(Optional.empty());

        sut = new FilingpackageApiDelegateImpl(filingPackageService);
    }

    @Test
    @DisplayName("200: ok action required details returned")
    public void withValidRequestReturnActionRequiredDetails() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<ActionRequiredDetails> result = sut.getActionRequiredDetails(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should return 403")
    public void withNoUniversalIdShouldReturn403() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getActionRequiredDetails(BigDecimal.ONE));
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());

    }

    @Test
    @DisplayName("404: when no filling package is found should throw FilingPackageNotFoundException")
    public void withValidRequestNotFoundShouldThrowFilingPackageNotFoundException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getActionRequiredDetails(BigDecimal.ONE));
        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());

    }

}

