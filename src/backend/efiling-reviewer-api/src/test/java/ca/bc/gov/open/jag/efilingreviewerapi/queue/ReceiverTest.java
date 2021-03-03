package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsApiDelegateImpl test suite")
public class ReceiverTest {

    Receiver sut;

    @Mock
    private StringRedisTemplate stringRedisTemplateMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.doNothing().when(stringRedisTemplateMock).convertAndSend(any(), any());

        sut = new Receiver(stringRedisTemplateMock);

    }

    @Test
    @DisplayName("Ok: Message added")
    public void withMessageAddedToRedis() {
        sut.receiveMessage("");

        Assertions.assertEquals(1, sut.getCount());

    }

}
