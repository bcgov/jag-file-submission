package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.model.Account;
import ca.bc.gov.open.jag.efilingapi.api.model.UserDetails;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CsoAccountApiDelegateImpl Test Suite")
public class CsoAccountApiDelegateImplTest {

    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String EMAIL = "email@email.com";
    public static final String FIRST_NAME = "firstName";
    private CsoAccountApiDelegateImpl sut;

    @BeforeAll
    public void setUp() {
        sut = new CsoAccountApiDelegateImpl();
    }


    @Test
    @DisplayName("201: should return an account with cso")
    public void whenAccountCreatedShouldReturn201() {


        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(LAST_NAME);
        userDetails.setMiddleName(MIDDLE_NAME);
        userDetails.setEmail(EMAIL);
        userDetails.setFirstName(FIRST_NAME);
        Account account = new Account();
        account.setType(Account.TypeEnum.BCEID);
        account.setIdentifier(UUID.randomUUID().toString());
        userDetails.addAccountsItem(account);
        ResponseEntity<UserDetails> actual = sut.createAccount(userDetails);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertEquals(LAST_NAME, actual.getBody().getLastName());
        Assertions.assertEquals(MIDDLE_NAME, actual.getBody().getMiddleName());
        Assertions.assertEquals(EMAIL, actual.getBody().getEmail());
        Assertions.assertEquals(FIRST_NAME, actual.getBody().getFirstName());

    }

}
