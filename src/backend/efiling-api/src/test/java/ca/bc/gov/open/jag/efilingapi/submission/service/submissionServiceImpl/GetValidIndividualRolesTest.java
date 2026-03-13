package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.api.model.InitialDocument;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetValidIndividualRolesTest {

    private static String[] ROLE_TYPES = new String[] {"role1", "role2", "role3", "role4", "role5"};

    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private EfilingLookupService efilingLookupService;

    @Mock
    private EfilingCourtService efilingCourtService;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private EfilingSubmissionService efilingSubmissionServiceMock;

    @Mock
    private PaymentAdapter paymentAdapterMock;

    @Mock
    private SftpService sftpServiceMock;


    @BeforeAll
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        Mockito.when(efilingLookupService.getValidPartyRoles(
                Mockito.eq("A"),
                Mockito.eq("B"),
                Mockito.eq("POR,ACMW"),
                any())).thenReturn(Arrays.asList(ROLE_TYPES));
        NavigationProperties navigationProperties = new NavigationProperties();
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, new PartyMapperImpl(), efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, null, documentStoreMock, paymentAdapterMock, sftpServiceMock, navigationProperties);
    }

    @Test
    @DisplayName("ok: service will return a valid list of role types")
    public void shouldReturnAValidListOfTypes()
    {

        List<InitialDocument> documents = new ArrayList<>();
        InitialDocument document1 = new InitialDocument();
        document1.setType("POR");
        documents.add(document1);
        InitialDocument document2 = new InitialDocument();
        document2.setType("ACMW");
        documents.add(document2);
        GetValidPartyRoleRequest getValidPartyRoleRequest = GetValidPartyRoleRequest
                .builder()
                .courtLevel("A")
                .courtClassification("B")
                .documents(documents)
                .create();

        List<String> actual = sut.getValidPartyRoles(getValidPartyRoleRequest);

        Assertions.assertEquals(5, actual.size());
        Assertions.assertTrue(Arrays.asList(ROLE_TYPES).containsAll(actual));

    }

}
