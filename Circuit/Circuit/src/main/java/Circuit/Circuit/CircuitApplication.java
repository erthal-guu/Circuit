package Circuit.Circuit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CircuitApplication {
	public static void main(String[] args) {
		SpringApplication.run(CircuitApplication.class, args);
	}
}
