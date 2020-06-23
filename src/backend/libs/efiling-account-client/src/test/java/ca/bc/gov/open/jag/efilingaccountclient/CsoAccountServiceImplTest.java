package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;

import org.junit.jupiter.api.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CsoAccountServiceImplTest {

    private static final String USERGUID = "User GUID";

    @InjectMocks
    CsoAccountServiceImpl sut;

    @Mock
    AccountFacade mockAccountFacade;

    @Mock
    AccountFacadeBean mockAccountFacadeBean;

    @BeforeAll
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        CsoAccountDetails details = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN);
        ClientProfile profile =  new ClientProfile();
        profile.setAccountId(BigDecimal.TEN);
        profile.setClientId(BigDecimal.TEN);
        List<ClientProfile> profiles = new ArrayList<ClientProfile>();
        profiles.add(profile);

        Mockito.when(mockAccountFacade.getAccountFacadeBeanPort()).thenReturn(mockAccountFacadeBean);
        Mockito.when(mockAccountFacadeBean.findProfiles(any())).thenReturn(profiles);
      }

    @DisplayName("CASE 1: getAccountDetails called with empty userGuid")
    @Test
    public void testWithEmptyUserGuid() {

        CsoAccountDetails details = sut.getAccountDetails("");
        Assertions.assertEquals(null, details);
        verify(mockAccountFacade, times(0)).getAccountFacadeBeanPort();
    }

//    @DisplayName("CASE 2: getAccountDetails called with any non-empty userGuid")
//    @Test
//    public void testWithPopulatedUserGuid() throws NestedEjbException_Exception {
//
//        CsoAccountDetails details = sut.getAccountDetails(USERGUID);
//        Assertions.assertNotEquals(null, details);
//        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
//        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
//        verify(mockAccountFacade, times(1)).getAccountFacadeBeanPort();
//        verify(mockAccountFacadeBean, times(1)).findProfiles(any());
//    }
}
