package ca.bc.gov.open.jag.aireviewercsoapi.extract;

import ca.bc.gov.open.jag.aireviewercsoapi.api.model.ExtractNotification;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtractApiDelegateImplTest {

    ExtractApiDelegateImpl sut;

    @BeforeAll
    public void beforeAll() {

        sut = new ExtractApiDelegateImpl();

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withRequestReturnResult() {

        ResponseEntity actual = sut.extractNotification(UUID.randomUUID(), new ExtractNotification());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }
}
