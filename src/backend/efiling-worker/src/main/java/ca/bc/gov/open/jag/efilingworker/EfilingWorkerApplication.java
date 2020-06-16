package ca.bc.gov.open.jag.efilingworker;

import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class EfilingWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EfilingWorkerApplication.class, args);
	}

}
