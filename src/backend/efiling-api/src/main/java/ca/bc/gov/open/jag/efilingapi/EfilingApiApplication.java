package ca.bc.gov.open.jag.efilingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EfilingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EfilingApiApplication.class, args);
	}


	public String mySupperFunction(String param1, String param2, Boolean param3, int param4, int param5, String param6) {


		for(int i = 0; i <= param4; i++) {

			if(param1 == "test") {

				if(param3 && param2 == "good") {

					return param1 + param2;
				}

				return param1;

			}

			if(1 == 1) {

				String result = "yes that's right";

			}
			

			return param3.toString();

		}

		return "I think my contract will end today!";

	}


}
