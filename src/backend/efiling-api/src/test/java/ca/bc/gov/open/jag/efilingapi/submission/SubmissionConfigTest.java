package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.*;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubmissionConfigTest {


    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(
                    SubmissionConfig.class,
                    CacheProperties.class)
            .withBean(ApiClient.class)
            .withBean(PaymentsApi.class)
            .withBean(BamboraPaymentAdapter.class)
            .withBean(SftpServiceTestImpl.class)
            .withBean(EfilingLookupServiceTest.class)
            .withBean(EfilingCourtServiceTest.class)
            .withBean(EfilingDocumentServiceTest.class)
            .withBean(EfilingSubmissionServiceTest.class)
            .withBean(DocumentStoreTest.class);

    @Test
    @DisplayName("Test Submission Beans")
    public void testSubmissionBeans() {

        context.run(it -> {
            assertThat(it).hasSingleBean(SubmissionStore.class);
            Assertions.assertEquals(SubmissionStoreImpl.class, it.getBean(SubmissionStore.class).getClass());

            assertThat(it).hasSingleBean(SubmissionMapper.class);
            Assertions.assertEquals(SubmissionMapperImpl.class, it.getBean(SubmissionMapper.class).getClass());

            assertThat(it).hasSingleBean(FilingPackageMapper.class);
            Assertions.assertEquals(FilingPackageMapperImpl.class, it.getBean(FilingPackageMapper.class).getClass());


            assertThat(it).hasSingleBean(SubmissionService.class);
            Assertions.assertEquals(SubmissionServiceImpl.class, it.getBean(SubmissionService.class).getClass());


        });

    }



    public static class SftpServiceTestImpl implements SftpService {

        @Override
        public ByteArrayInputStream getContent(String s) {
            return null;
        }

        @Override
        public void moveFile(String s, String s1) {
        }

        @Override
        public void put(InputStream inputStream, String s) {
        }

        @Override
        public List<String> listFiles(String s) {
            return null;
        }
    }


    public static class EfilingLookupServiceTest implements EfilingLookupService {

        @Override
        public ServiceFees getServiceFee(String serviceId) {
            return null;
        }
    }

    public static class EfilingCourtServiceTest implements EfilingCourtService {

        @Override
        public CourtDetails getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass) {
            return null;
        }

        @Override
        public boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass) {
            return false;
        }
    }

    public static class EfilingSubmissionServiceTest implements EfilingSubmissionService {

        @Override
        public SubmitPackageResponse submitFilingPackage(AccountDetails accountDetails, FilingPackage efilingPackage, EfilingPaymentService payment) {
            return null;
        }
    }

    public static class EfilingDocumentServiceTest implements EfilingDocumentService {

        @Override
        public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
            return null;
        }

        @Override
        public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
            return null;
        }
    }

    public static class DocumentStoreTest implements DocumentStore {


        @Override
        public byte[] put(SubmissionKey submissionKey, String fileName, byte[] content) {
            return new byte[0];
        }

        @Override
        public byte[] get(SubmissionKey submissionKey, String fileName) {
            return new byte[0];
        }

        @Override
        public void evict(SubmissionKey submissionKey, String fileName) {

        }

        @Override
        public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
            return null;
        }

        @Override
        public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
            return null;
        }
    }
    
}
