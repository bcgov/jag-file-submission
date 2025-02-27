package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeedAccountTest {

    private SeedAccount sut;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;

    @Mock
    private ContextRefreshedEvent eventMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(efilingAccountServiceMock.createAccount(Mockito.any())).thenReturn(AccountDetails.builder().universalId(UUID.randomUUID().toString()).create());
        sut = new SeedAccount(efilingAccountServiceMock);
    }

    //@Test
    @DisplayName("OK: on application start should seed 2 accounts")
    public void whenInitShouldCreate2Accounts() {

        sut.onApplicationEvent(eventMock);

        Mockito.verify(efilingAccountServiceMock, Mockito.times(2))
                .createAccount(Mockito.any());

    }

}
