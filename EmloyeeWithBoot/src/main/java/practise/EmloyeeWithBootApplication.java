package practise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages="practise")
@SpringBootApplication
public class EmloyeeWithBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmloyeeWithBootApplication.class, args);
	}
}
