package tqs.ua.tqs01proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Tqs01projApplication {

	@Bean	// dizer que Beans functions as singletons
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}
	// https://stackoverflow.com/questions/57809861/sonarqube-rule-using-command-line-arguments-is-security-sensitive-in-spring-b
	public static void main(String[] args) {
		SpringApplication.run(Tqs01projApplication.class, args);
	}

}
