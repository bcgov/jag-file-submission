package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
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

import java.util.*;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")

public class GetFilingPackagesTest {

    public static final String PARENT_APPLICATION_FOUND = "FOUND";
    public static final String PARENT_APPLICATION_NOT_FOUND = "NOT_FOUND";
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

        Mockito.when(filingPackageService.getFilingPackages(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING), ArgumentMatchers.eq(PARENT_APPLICATION_FOUND))).thenReturn(Optional.of(new ArrayList<>()));

        Mockito.when(filingPackageService.getFilingPackages(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING), ArgumentMatchers.eq(PARENT_APPLICATION_FOUND))).thenReturn(Optional.empty());

        sut = new FilingpackageApiDelegateImpl(filingPackageService);
    }

    @Test
    @DisplayName("200: ok filingpackages returned")
    public void withValidRequestReturnFilingPackages() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<List<FilingPackage>> actual = sut.getFilingPackages(PARENT_APPLICATION_FOUND);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should throw MissingUniversalIdException")
    public void withNoUniversalIdShouldThrowMissingUniversalIdException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);


        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getFilingPackages(PARENT_APPLICATION_FOUND));
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());

    }

    @Test
    @DisplayName("404: when no filling packages found should throw FilingPackageNotFoundException")
    public void withValidRequestNotFoundShouldThrowFilingPackageNotFoundException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);


        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getFilingPackages(PARENT_APPLICATION_NOT_FOUND));
        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());

    }

}
