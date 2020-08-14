package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.model.BceidAccount;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BceidAccountApiDelegateImplTest {

    private BceidAccountApiDelagateImpl sut;

    @BeforeAll
    public void setup() {

        sut = new BceidAccountApiDelagateImpl();

    }

    @Test
    @DisplayName("200: should return bceid account")
    public void withAccountShouldReturnAccount() {

        ResponseEntity<BceidAccount> actual = sut.getBceidAccount(UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("Bob", actual.getBody().getFirstName());
        Assertions.assertEquals("Alan", actual.getBody().getMiddleName());
        Assertions.assertEquals("Ross", actual.getBody().getLastName());

    }


}
