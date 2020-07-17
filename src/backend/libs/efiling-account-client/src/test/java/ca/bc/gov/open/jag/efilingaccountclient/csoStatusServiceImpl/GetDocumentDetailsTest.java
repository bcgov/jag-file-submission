package ca.bc.gov.open.jag.efilingaccountclient.csoStatusServiceImpl;

import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingaccountclient.CSOStatusServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Get Document Details Test Suite")
public class GetDocumentDetailsTest {

    @Mock
    FilingStatusFacadeBean filingStatusFacadeBean;

    private static CSOStatusServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        sut = new CSOStatusServiceImpl(filingStatusFacadeBean);
    }

    @DisplayName("OK: test returns null ")
    @Test
    public void testWithFileRoleEnabled() {
        Assertions.assertNull(sut.getDocumentDetails(""));
    }
}
