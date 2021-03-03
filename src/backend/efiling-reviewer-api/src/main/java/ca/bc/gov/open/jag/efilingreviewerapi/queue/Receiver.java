package ca.bc.gov.open.jag.efilingreviewerapi.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final AtomicInteger counter = new AtomicInteger();

    public Receiver(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void receiveMessage(String message) {
        logger.debug("Message received");
        counter.incrementAndGet();
        stringRedisTemplate.convertAndSend(counter.toString(), message);
        logger.info("Message {} put on cache", counter.get());
    }

    public int getCount() {
        return counter.get();
    }
}
