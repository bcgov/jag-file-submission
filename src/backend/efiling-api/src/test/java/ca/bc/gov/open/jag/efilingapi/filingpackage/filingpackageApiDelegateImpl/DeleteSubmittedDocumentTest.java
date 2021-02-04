package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilepackageApiDelegateImplTest")
public class DeleteSubmittedDocumentTest {

    FilingpackageApiDelegateImpl sut;

    @Mock
    FilingPackageService filingPackageService;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        Mockito.doNothing().when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_ONE));

        Mockito.doThrow(RuntimeException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));

        Mockito.doThrow(EfilingAccountServiceException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.TEN), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));

        sut = new FilingpackageApiDelegateImpl(filingPackageService);
    }

    @Test
    @DisplayName("200: ok document was deleted")
    public void withValidRequestReturnFilingPackage() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<?> result = sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should return 403")
    public void withNoUniversalIdShouldReturn403() {

        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);

        ResponseEntity<?> actual = sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }

    @Test
    @DisplayName("400: when delete failed should return 400")
    public void withDeleteFailedShouldReturn400() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<?> actual = sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: when delete failed should return 404")
    public void withDeleteAccountNotFoundFailedShouldReturn404() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<?> actual = sut.deleteSubmittedDocument(BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

}
