package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingReviewServiceException;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

public class GetSubmissionDocumentTest {


    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    private static CsoReviewServiceImpl sut;

    @BeforeEach
    public void beforeEach() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);

        byte[] someBytes = "test".getBytes();

        Mockito.when(restTemplateMock.getForEntity(
                Mockito.eq("http://localhost/acdc/1"), Mockito.eq(byte[].class), Mockito.any(HttpEntity.class)))
          .thenReturn(new ResponseEntity(someBytes, HttpStatus.OK));

        Mockito.when(restTemplateMock.getForEntity(
                Mockito.eq("http://localhost/acdc/1"), Mockito.eq(byte[].class), Mockito.any(HttpEntity.class)))
                .thenReturn(new ResponseEntity(someBytes, HttpStatus.OK));

        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setCsoBasePath("http://locahost:8080");

        sut = new CsoReviewServiceImpl(null, null, filingFacadeBeanMock, new FilePackageMapperImpl(), csoProperties, restTemplateMock);

    }

    @DisplayName("OK: it is working")
    @Test
    public void testWithFoundResult() {

        Optional<byte[]> actual = sut.getSubmittedDocument(BigDecimal.ONE);
        Assertions.assertFalse(actual.isPresent());

    }


}
