package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.error.DeleteDocumentException;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.MissingUniversalIdException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
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

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
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
    private Jwt jwtMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        Mockito.doNothing().when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_ONE));

        Mockito.doThrow(RuntimeException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));

        Mockito.doThrow(EfilingAccountServiceException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.TEN), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));

        sut = new FilingpackageApiDelegateImpl(filingPackageService);
    }

    @Test
    @DisplayName("200: ok document was deleted")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<?> result = sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should throw MissingUniversalIdException")
    public void withNoUniversalIdShouldThrowMissingUniversalIdException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE));
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("400: when delete failed should throw DeleteDocumentException")
    public void withDeleteFailedShouldThrowDeleteDocumentException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        DeleteDocumentException exception = Assertions.assertThrows(DeleteDocumentException.class, () -> sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO));
        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(), exception.getErrorCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    @DisplayName("404: when delete failed should throw DeleteDocumentException")
    public void withDeleteAccountNotFoundFailedShouldThrowDeleteDocumentException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        DeleteDocumentException exception = Assertions.assertThrows(DeleteDocumentException.class, () -> sut.deleteSubmittedDocument(BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO));
        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(), exception.getErrorCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

}
